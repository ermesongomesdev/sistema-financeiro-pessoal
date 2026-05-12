package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.household.AddMemberRequest;
import br.com.veltrium.finance.dto.household.HouseholdRequest;
import br.com.veltrium.finance.dto.household.HouseholdResponse;
import br.com.veltrium.finance.dto.household.MemberResponse;
import br.com.veltrium.finance.exception.ApiException;
import br.com.veltrium.finance.model.AppUser;
import br.com.veltrium.finance.model.Household;
import br.com.veltrium.finance.model.HouseholdMember;
import br.com.veltrium.finance.model.enums.MemberRole;
import br.com.veltrium.finance.repository.AppUserRepository;
import br.com.veltrium.finance.repository.HouseholdMemberRepository;
import br.com.veltrium.finance.repository.HouseholdRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HouseholdService {
    private final HouseholdRepository householdRepository;
    private final HouseholdMemberRepository memberRepository;
    private final AppUserRepository userRepository;
    private final UserContextService userContextService;

    public HouseholdService(HouseholdRepository householdRepository, HouseholdMemberRepository memberRepository, AppUserRepository userRepository, UserContextService userContextService) {
        this.householdRepository = householdRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.userContextService = userContextService;
    }

    public List<HouseholdResponse> listMine() {
        AppUser current = userContextService.currentUser();
        return memberRepository.findByUserId(current.getId()).stream()
                .map(HouseholdMember::getHousehold)
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public HouseholdResponse create(HouseholdRequest request) {
        AppUser current = userContextService.currentUser();
        Household household = new Household();
        household.setName(request.name());
        household.setCreatedBy(current);
        householdRepository.save(household);

        HouseholdMember member = new HouseholdMember();
        member.setHousehold(household);
        member.setUser(current);
        member.setRole(MemberRole.OWNER);
        memberRepository.save(member);

        return toResponse(household);
    }

    @Transactional
    public HouseholdResponse addMember(Long householdId, AddMemberRequest request) {
        AppUser current = userContextService.currentUser();
        Household household = getOwnedHousehold(householdId, current.getId());
        AppUser userToAdd = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> ApiException.notFound("Usuário convidado não encontrado. Ele precisa se cadastrar primeiro."));

        if (memberRepository.existsByHouseholdIdAndUserId(householdId, userToAdd.getId())) {
            throw ApiException.badRequest("Este usuário já participa desta carteira");
        }

        HouseholdMember member = new HouseholdMember();
        member.setHousehold(household);
        member.setUser(userToAdd);
        member.setRole(request.role());
        memberRepository.save(member);
        return toResponse(household);
    }

    public Household getMemberHousehold(Long householdId) {
        AppUser current = userContextService.currentUser();
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> ApiException.notFound("Carteira não encontrada"));
        if (!memberRepository.existsByHouseholdIdAndUserId(householdId, current.getId())) {
            throw ApiException.forbidden("Você não tem acesso a esta carteira");
        }
        return household;
    }

    public Household getOwnedHousehold(Long householdId, Long userId) {
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> ApiException.notFound("Carteira não encontrada"));
        var member = memberRepository.findByHouseholdIdAndUserId(householdId, userId)
                .orElseThrow(() -> ApiException.forbidden("Você não tem acesso a esta carteira"));
        if (member.getRole() != MemberRole.OWNER) {
            throw ApiException.forbidden("Apenas o dono pode alterar membros desta carteira");
        }
        return household;
    }

    public void assertMember(Long householdId) {
        getMemberHousehold(householdId);
    }

    public HouseholdResponse toResponse(Household household) {
        List<MemberResponse> members = memberRepository.findByHouseholdId(household.getId()).stream()
                .map(member -> new MemberResponse(
                        member.getUser().getId(),
                        member.getUser().getName(),
                        member.getUser().getEmail(),
                        member.getRole()
                ))
                .toList();
        return new HouseholdResponse(household.getId(), household.getName(), members);
    }
}
