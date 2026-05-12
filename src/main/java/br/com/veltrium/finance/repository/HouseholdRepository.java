package br.com.veltrium.finance.repository;

import br.com.veltrium.finance.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseholdRepository extends JpaRepository<Household, Long> {
}
