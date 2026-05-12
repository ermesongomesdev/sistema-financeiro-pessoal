package br.com.veltrium.finance.repository;

import br.com.veltrium.finance.model.Category;
import br.com.veltrium.finance.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByHouseholdIdOrderByNameAsc(Long householdId);
    boolean existsByHouseholdIdAndNameIgnoreCaseAndType(Long householdId, String name, TransactionType type);
}
