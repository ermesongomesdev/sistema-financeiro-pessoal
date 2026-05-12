package br.com.veltrium.finance.repository;

import br.com.veltrium.finance.model.TransactionEntry;
import br.com.veltrium.finance.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntry, Long> {
    List<TransactionEntry> findByHouseholdIdOrderByDueDateDesc(Long householdId);
    List<TransactionEntry> findByHouseholdIdAndDueDateBetweenOrderByDueDateAsc(Long householdId, LocalDate start, LocalDate end);
    List<TransactionEntry> findByHouseholdIdAndPaidFalseAndDueDateBetweenOrderByDueDateAsc(Long householdId, LocalDate start, LocalDate end);

    @Query("select coalesce(sum(t.amount), 0) from TransactionEntry t where t.household.id = :householdId and t.type = :type and t.dueDate between :start and :end")
    BigDecimal sumByTypeAndPeriod(@Param("householdId") Long householdId, @Param("type") TransactionType type, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
