package br.com.veltrium.finance.repository;

import br.com.veltrium.finance.model.DebtInstallment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface DebtInstallmentRepository extends JpaRepository<DebtInstallment, Long> {
    List<DebtInstallment> findByDebtHouseholdIdAndPaidFalseAndDueDateBetween(Long householdId, LocalDate start, LocalDate end);
}
