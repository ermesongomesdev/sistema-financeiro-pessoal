package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.debt.DebtInstallmentResponse;
import br.com.veltrium.finance.dto.debt.DebtRequest;
import br.com.veltrium.finance.dto.debt.DebtResponse;
import br.com.veltrium.finance.exception.ApiException;
import br.com.veltrium.finance.model.Debt;
import br.com.veltrium.finance.model.DebtInstallment;
import br.com.veltrium.finance.repository.DebtInstallmentRepository;
import br.com.veltrium.finance.repository.DebtRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class DebtService {
    private final DebtRepository debtRepository;
    private final DebtInstallmentRepository installmentRepository;
    private final HouseholdService householdService;
    private final UserContextService userContextService;

    public DebtService(DebtRepository debtRepository, DebtInstallmentRepository installmentRepository, HouseholdService householdService, UserContextService userContextService) {
        this.debtRepository = debtRepository;
        this.installmentRepository = installmentRepository;
        this.householdService = householdService;
        this.userContextService = userContextService;
    }

    public List<DebtResponse> list(Long householdId) {
        householdService.assertMember(householdId);
        return debtRepository.findByHouseholdIdOrderByCreatedAtDesc(householdId).stream()
                .peek(Debt::refreshStatus)
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public DebtResponse create(DebtRequest request) {
        var household = householdService.getMemberHousehold(request.householdId());
        Debt debt = new Debt();
        debt.setHousehold(household);
        debt.setCreatedBy(userContextService.currentUser());
        debt.setDescription(request.description());
        debt.setCreditor(request.creditor());
        debt.setTotalAmount(request.totalAmount());
        debt.setInstallments(request.installments());
        debt.setFirstDueDate(request.firstDueDate());

        BigDecimal installmentValue = request.totalAmount().divide(BigDecimal.valueOf(request.installments()), 2, RoundingMode.HALF_UP);
        BigDecimal accumulated = BigDecimal.ZERO;
        for (int i = 1; i <= request.installments(); i++) {
            DebtInstallment installment = new DebtInstallment();
            installment.setDebt(debt);
            installment.setNumber(i);
            if (i == request.installments()) {
                installment.setAmount(request.totalAmount().subtract(accumulated));
            } else {
                installment.setAmount(installmentValue);
                accumulated = accumulated.add(installmentValue);
            }
            installment.setDueDate(request.firstDueDate().plusMonths(i - 1L));
            installment.setPaid(false);
            debt.getInstallmentList().add(installment);
        }
        debt.refreshStatus();
        return toResponse(debtRepository.save(debt));
    }

    @Transactional
    public DebtResponse payInstallment(Long installmentId, Long householdId) {
        householdService.assertMember(householdId);
        DebtInstallment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> ApiException.notFound("Parcela não encontrada"));
        Debt debt = installment.getDebt();
        if (!debt.getHousehold().getId().equals(householdId)) {
            throw ApiException.forbidden("Parcela não pertence a esta carteira");
        }
        if (installment.isPaid()) {
            throw ApiException.badRequest("Parcela já está paga");
        }
        installment.setPaid(true);
        installment.setPaidAt(LocalDate.now());
        debt.setPaidAmount(debt.getPaidAmount().add(installment.getAmount()));
        debt.refreshStatus();
        return toResponse(debtRepository.save(debt));
    }

    @Transactional
    public void delete(Long id, Long householdId) {
        householdService.assertMember(householdId);
        Debt debt = debtRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Dívida não encontrada"));
        if (!debt.getHousehold().getId().equals(householdId)) {
            throw ApiException.forbidden("Dívida não pertence a esta carteira");
        }
        debtRepository.delete(debt);
    }

    public DebtResponse toResponse(Debt debt) {
        debt.refreshStatus();
        List<DebtInstallmentResponse> installments = debt.getInstallmentList().stream()
                .map(i -> new DebtInstallmentResponse(i.getId(), i.getNumber(), i.getAmount(), i.getDueDate(), i.isPaid(), i.getPaidAt()))
                .toList();
        return new DebtResponse(
                debt.getId(),
                debt.getDescription(),
                debt.getCreditor(),
                debt.getTotalAmount(),
                debt.getPaidAmount(),
                debt.getInstallments(),
                debt.getFirstDueDate(),
                debt.getStatus(),
                installments
        );
    }
}
