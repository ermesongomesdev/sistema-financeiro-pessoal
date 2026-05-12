package br.com.veltrium.finance.controller;

import br.com.veltrium.finance.dto.debt.DebtRequest;
import br.com.veltrium.finance.dto.debt.DebtResponse;
import br.com.veltrium.finance.service.DebtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/debts")
public class DebtController {
    private final DebtService debtService;

    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

    @GetMapping
    public List<DebtResponse> list(@RequestParam Long householdId) {
        return debtService.list(householdId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DebtResponse create(@Valid @RequestBody DebtRequest request) {
        return debtService.create(request);
    }

    @PatchMapping("/installments/{installmentId}/pay")
    public DebtResponse payInstallment(@PathVariable Long installmentId, @RequestParam Long householdId) {
        return debtService.payInstallment(installmentId, householdId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @RequestParam Long householdId) {
        debtService.delete(id, householdId);
    }
}
