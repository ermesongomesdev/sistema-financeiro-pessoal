package br.com.veltrium.finance.repository;

import br.com.veltrium.finance.model.Debt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByHouseholdIdOrderByCreatedAtDesc(Long householdId);
}
