package br.com.veltrium.finance.controller;

import br.com.veltrium.finance.dto.goal.GoalDepositRequest;
import br.com.veltrium.finance.dto.goal.GoalRequest;
import br.com.veltrium.finance.dto.goal.GoalResponse;
import br.com.veltrium.finance.service.GoalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping
    public List<GoalResponse> list(@RequestParam Long householdId) {
        return goalService.list(householdId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse create(@Valid @RequestBody GoalRequest request) {
        return goalService.create(request);
    }

    @PatchMapping("/{id}/deposit")
    public GoalResponse deposit(@PathVariable Long id, @RequestParam Long householdId, @Valid @RequestBody GoalDepositRequest request) {
        return goalService.deposit(id, householdId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @RequestParam Long householdId) {
        goalService.delete(id, householdId);
    }
}
