package br.com.veltrium.finance.controller;

import br.com.veltrium.finance.dto.household.AddMemberRequest;
import br.com.veltrium.finance.dto.household.HouseholdRequest;
import br.com.veltrium.finance.dto.household.HouseholdResponse;
import br.com.veltrium.finance.service.HouseholdService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/households")
public class HouseholdController {
    private final HouseholdService householdService;

    public HouseholdController(HouseholdService householdService) {
        this.householdService = householdService;
    }

    @GetMapping
    public List<HouseholdResponse> listMine() {
        return householdService.listMine();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HouseholdResponse create(@Valid @RequestBody HouseholdRequest request) {
        return householdService.create(request);
    }

    @PostMapping("/{id}/members")
    public HouseholdResponse addMember(@PathVariable Long id, @Valid @RequestBody AddMemberRequest request) {
        return householdService.addMember(id, request);
    }
}
