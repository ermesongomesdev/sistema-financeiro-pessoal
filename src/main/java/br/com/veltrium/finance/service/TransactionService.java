package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.transaction.TransactionRequest;
import br.com.veltrium.finance.dto.transaction.TransactionResponse;
import br.com.veltrium.finance.exception.ApiException;
import br.com.veltrium.finance.model.Category;
import br.com.veltrium.finance.model.TransactionEntry;
import br.com.veltrium.finance.repository.CategoryRepository;
import br.com.veltrium.finance.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final HouseholdService householdService;
    private final UserContextService userContextService;

    public TransactionService(TransactionRepository transactionRepository, CategoryRepository categoryRepository, HouseholdService householdService, UserContextService userContextService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.householdService = householdService;
        this.userContextService = userContextService;
    }

    public List<TransactionResponse> list(Long householdId) {
        householdService.assertMember(householdId);
        return transactionRepository.findByHouseholdIdOrderByDueDateDesc(householdId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public TransactionResponse create(TransactionRequest request) {
        var household = householdService.getMemberHousehold(request.householdId());
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> ApiException.notFound("Categoria não encontrada"));
        if (!category.getHousehold().getId().equals(request.householdId())) {
            throw ApiException.badRequest("Categoria não pertence à carteira informada");
        }
        if (category.getType() != request.type()) {
            throw ApiException.badRequest("O tipo da transação precisa ser igual ao tipo da categoria");
        }

        TransactionEntry entry = new TransactionEntry();
        entry.setHousehold(household);
        entry.setCategory(category);
        entry.setCreatedBy(userContextService.currentUser());
        apply(entry, request);
        return toResponse(transactionRepository.save(entry));
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionRequest request) {
        householdService.assertMember(request.householdId());
        TransactionEntry entry = transactionRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Transação não encontrada"));
        if (!entry.getHousehold().getId().equals(request.householdId())) {
            throw ApiException.forbidden("Transação não pertence a esta carteira");
        }
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> ApiException.notFound("Categoria não encontrada"));
        if (!category.getHousehold().getId().equals(request.householdId())) {
            throw ApiException.badRequest("Categoria não pertence à carteira informada");
        }
        entry.setCategory(category);
        apply(entry, request);
        return toResponse(transactionRepository.save(entry));
    }

    @Transactional
    public TransactionResponse markPaid(Long id, Long householdId) {
        householdService.assertMember(householdId);
        TransactionEntry entry = transactionRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Transação não encontrada"));
        if (!entry.getHousehold().getId().equals(householdId)) {
            throw ApiException.forbidden("Transação não pertence a esta carteira");
        }
        entry.setPaid(true);
        entry.setPaidAt(LocalDate.now());
        return toResponse(transactionRepository.save(entry));
    }

    @Transactional
    public void delete(Long id, Long householdId) {
        householdService.assertMember(householdId);
        TransactionEntry entry = transactionRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Transação não encontrada"));
        if (!entry.getHousehold().getId().equals(householdId)) {
            throw ApiException.forbidden("Transação não pertence a esta carteira");
        }
        transactionRepository.delete(entry);
    }

    private void apply(TransactionEntry entry, TransactionRequest request) {
        entry.setDescription(request.description());
        entry.setAmount(request.amount());
        entry.setType(request.type());
        entry.setDueDate(request.dueDate());
        entry.setPaid(request.paid());
        entry.setPaidAt(request.paid() ? LocalDate.now() : null);
    }

    public TransactionResponse toResponse(TransactionEntry entry) {
        return new TransactionResponse(
                entry.getId(),
                entry.getDescription(),
                entry.getAmount(),
                entry.getType(),
                entry.getDueDate(),
                entry.isPaid(),
                entry.getPaidAt(),
                entry.getCategory().getId(),
                entry.getCategory().getName()
        );
    }
}
