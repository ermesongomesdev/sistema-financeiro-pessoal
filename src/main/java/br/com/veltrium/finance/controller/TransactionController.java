package br.com.veltrium.finance.controller;

import br.com.veltrium.finance.dto.transaction.TransactionRequest;
import br.com.veltrium.finance.dto.transaction.TransactionResponse;
import br.com.veltrium.finance.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<TransactionResponse> list(@RequestParam Long householdId) {
        return transactionService.list(householdId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse create(@Valid @RequestBody TransactionRequest request) {
        return transactionService.create(request);
    }

    @PutMapping("/{id}")
    public TransactionResponse update(@PathVariable Long id, @Valid @RequestBody TransactionRequest request) {
        return transactionService.update(id, request);
    }

    @PatchMapping("/{id}/pay")
    public TransactionResponse markPaid(@PathVariable Long id, @RequestParam Long householdId) {
        return transactionService.markPaid(id, householdId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, @RequestParam Long householdId) {
        transactionService.delete(id, householdId);
    }
}
