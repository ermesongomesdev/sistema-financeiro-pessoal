package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.category.CategoryRequest;
import br.com.veltrium.finance.dto.category.CategoryResponse;
import br.com.veltrium.finance.exception.ApiException;
import br.com.veltrium.finance.model.Category;
import br.com.veltrium.finance.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final HouseholdService householdService;

    public CategoryService(CategoryRepository categoryRepository, HouseholdService householdService) {
        this.categoryRepository = categoryRepository;
        this.householdService = householdService;
    }

    public List<CategoryResponse> list(Long householdId) {
        householdService.assertMember(householdId);
        return categoryRepository.findByHouseholdIdOrderByNameAsc(householdId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        var household = householdService.getMemberHousehold(request.householdId());
        if (categoryRepository.existsByHouseholdIdAndNameIgnoreCaseAndType(request.householdId(), request.name(), request.type())) {
            throw ApiException.badRequest("Categoria já existe para este tipo");
        }
        Category category = new Category();
        category.setHousehold(household);
        category.setName(request.name());
        category.setType(request.type());
        category.setColor(request.color());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        householdService.assertMember(request.householdId());
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Categoria não encontrada"));
        if (!category.getHousehold().getId().equals(request.householdId())) {
            throw ApiException.forbidden("Categoria não pertence a esta carteira");
        }
        category.setName(request.name());
        category.setType(request.type());
        category.setColor(request.color());
        return toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void delete(Long id, Long householdId) {
        householdService.assertMember(householdId);
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> ApiException.notFound("Categoria não encontrada"));
        if (!category.getHousehold().getId().equals(householdId)) {
            throw ApiException.forbidden("Categoria não pertence a esta carteira");
        }
        categoryRepository.delete(category);
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType(), category.getColor());
    }
}
