package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.notification.NotificationResponse;
import br.com.veltrium.finance.exception.ApiException;
import br.com.veltrium.finance.model.Notification;
import br.com.veltrium.finance.model.enums.NotificationStatus;
import br.com.veltrium.finance.repository.DebtInstallmentRepository;
import br.com.veltrium.finance.repository.HouseholdMemberRepository;
import br.com.veltrium.finance.repository.NotificationRepository;
import br.com.veltrium.finance.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final TransactionRepository transactionRepository;
    private final DebtInstallmentRepository installmentRepository;
    private final HouseholdMemberRepository memberRepository;
    private final HouseholdService householdService;
    private final int daysBeforeDue;

    public NotificationService(
            NotificationRepository notificationRepository,
            TransactionRepository transactionRepository,
            DebtInstallmentRepository installmentRepository,
            HouseholdMemberRepository memberRepository,
            HouseholdService householdService,
            @Value("${app.notifications.days-before-due}") int daysBeforeDue
    ) {
        this.notificationRepository = notificationRepository;
        this.transactionRepository = transactionRepository;
        this.installmentRepository = installmentRepository;
        this.memberRepository = memberRepository;
        this.householdService = householdService;
        this.daysBeforeDue = daysBeforeDue;
    }

    public List<NotificationResponse> list(Long householdId) {
        householdService.assertMember(householdId);
        return notificationRepository.findByHouseholdIdOrderByCreatedAtDesc(householdId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public NotificationResponse markAsRead(Long notificationId, Long householdId) {
        householdService.assertMember(householdId);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> ApiException.notFound("Notificação não encontrada"));
        if (!notification.getHousehold().getId().equals(householdId)) {
            throw ApiException.forbidden("Notificação não pertence a esta carteira");
        }
        notification.setStatus(NotificationStatus.READ);
        return toResponse(notificationRepository.save(notification));
    }

    @Transactional
    public int generateForHousehold(Long householdId) {
        var household = householdService.getMemberHousehold(householdId);
        return createDueNotifications(household.getId());
    }

    @Scheduled(cron = "0 0 8 * * *")
    @Transactional
    public void generateDailyNotifications() {
        memberRepository.findAll().stream()
                .map(member -> member.getHousehold().getId())
                .distinct()
                .forEach(this::createDueNotifications);
    }

    private int createDueNotifications(Long householdId) {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(daysBeforeDue);
        int created = 0;

        var pendingTransactions = transactionRepository.findByHouseholdIdAndPaidFalseAndDueDateBetweenOrderByDueDateAsc(householdId, today, limit);
        for (var t : pendingTransactions) {
            Notification notification = new Notification();
            notification.setHousehold(t.getHousehold());
            notification.setTitle("Conta próxima do vencimento");
            notification.setMessage(t.getDescription() + " vence em " + t.getDueDate() + " no valor de R$ " + t.getAmount());
            notification.setDueDate(t.getDueDate());
            notificationRepository.save(notification);
            created++;
        }

        var pendingInstallments = installmentRepository.findByDebtHouseholdIdAndPaidFalseAndDueDateBetween(householdId, today, limit);
        for (var i : pendingInstallments) {
            Notification notification = new Notification();
            notification.setHousehold(i.getDebt().getHousehold());
            notification.setTitle("Parcela de dívida próxima do vencimento");
            notification.setMessage(i.getDebt().getDescription() + " - parcela " + i.getNumber() + " vence em " + i.getDueDate());
            notification.setDueDate(i.getDueDate());
            notificationRepository.save(notification);
            created++;
        }

        return created;
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getDueDate(),
                notification.getStatus(),
                notification.getCreatedAt()
        );
    }
}
