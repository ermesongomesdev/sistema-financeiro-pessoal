package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.goal.GoalDepositRequest;
import br.com.veltrium.finance.dto.goal.GoalRequest;
import br.com.veltrium.finance.dto.goal.GoalResponse;
import br.com.veltrium.finance.exception.ApiException;
import br.com.veltrium.finance.model.Goal;
import br.com.veltrium.finance.repository.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final HouseholdService householdService;

    public GoalService(GoalRepository goalRepository, HouseholdService householdService) {
        this.goalRepository = goalRepository;
        this.householdService = householdService;
    }

    public List<GoalResponse> list(Long householdId) {
        householdService.assertMember(householdId);
        return goalRepository.findByHouseholdIdOrderByCreatedAtDesc(householdId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public GoalResponse create(GoalRequest request) {
        var household = householdService.getMemberHousehold(request.householdId());
        Goal goal = new Goal();
        goal.setHousehold(household);
        goal.setName(request.name());
        goal.setTargetAmount(request.targetAmount());
        goal.setCurrentAmount(request.currentAmount() == null ? BigDecimal.ZERO : request.currentAmount());
        goal.setDeadline(request.deadline());
        return toResponse(goalRepository.save(goal));
    }

    @Transactional
    public GoalResponse deposit(Long id, Long householdId, GoalDepositRequest request) {
        householdService.assertMember(householdId);
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Meta não encontrada"));
        if (!goal.getHousehold().getId().equals(householdId)) {
            throw ApiException.forbidden("Meta não pertence a esta carteira");
        }
        goal.setCurrentAmount(goal.getCurrentAmount().add(request.amount()));
        return toResponse(goalRepository.save(goal));
    }

    @Transactional
    public void delete(Long id, Long householdId) {
        householdService.assertMember(householdId);
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Meta não encontrada"));
        if (!goal.getHousehold().getId().equals(householdId)) {
            throw ApiException.forbidden("Meta não pertence a esta carteira");
        }
        goalRepository.delete(goal);
    }

    public GoalResponse toResponse(Goal goal) {
        BigDecimal progress = goal.getCurrentAmount()
                .multiply(BigDecimal.valueOf(100))
                .divide(goal.getTargetAmount(), 2, RoundingMode.HALF_UP);
        return new GoalResponse(goal.getId(), goal.getName(), goal.getTargetAmount(), goal.getCurrentAmount(), progress, goal.getDeadline());
    }
}
