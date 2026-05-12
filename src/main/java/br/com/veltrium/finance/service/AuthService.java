package br.com.veltrium.finance.service;

import br.com.veltrium.finance.dto.auth.AuthResponse;
import br.com.veltrium.finance.dto.auth.LoginRequest;
import br.com.veltrium.finance.dto.auth.RegisterRequest;
import br.com.veltrium.finance.exception.ApiException;
import br.com.veltrium.finance.model.AppUser;
import br.com.veltrium.finance.model.Household;
import br.com.veltrium.finance.model.HouseholdMember;
import br.com.veltrium.finance.model.enums.MemberRole;
import br.com.veltrium.finance.model.enums.Role;
import br.com.veltrium.finance.repository.AppUserRepository;
import br.com.veltrium.finance.repository.HouseholdMemberRepository;
import br.com.veltrium.finance.repository.HouseholdRepository;
import br.com.veltrium.finance.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final AppUserRepository userRepository;
    private final HouseholdRepository householdRepository;
    private final HouseholdMemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository userRepository,
            HouseholdRepository householdRepository,
            HouseholdMemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.householdRepository = householdRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw ApiException.badRequest("Este e-mail já está cadastrado");
        }

        AppUser user = new AppUser();
        user.setName(request.name());
        user.setEmail(request.email().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        userRepository.save(user);

        Household household = new Household();
        household.setName("Carteira de " + request.name());
        household.setCreatedBy(user);
        householdRepository.save(household);

        HouseholdMember member = new HouseholdMember();
        member.setHousehold(household);
        member.setUser(user);
        member.setRole(MemberRole.OWNER);
        memberRepository.save(member);

        String token = jwtService.generateToken(User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).build());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email().toLowerCase(), request.password())
        );
        AppUser user = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> ApiException.notFound("Usuário não encontrado"));
        String token = jwtService.generateToken(User.withUsername(user.getEmail()).password(user.getPassword()).roles(user.getRole().name()).build());
        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail());
    }
}
