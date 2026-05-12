package br.com.veltrium.finance.repository;

import br.com.veltrium.finance.model.Notification;
import br.com.veltrium.finance.model.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByHouseholdIdOrderByCreatedAtDesc(Long householdId);
    long countByHouseholdIdAndStatus(Long householdId, NotificationStatus status);
}
