package br.com.veltrium.finance.repository;

import br.com.veltrium.finance.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByHouseholdIdOrderByCreatedAtDesc(Long householdId);
}
