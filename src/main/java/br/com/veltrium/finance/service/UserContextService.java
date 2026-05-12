package br.com.veltrium.finance.service;

import br.com.veltrium.finance.exception.ApiException;
import br.com.veltrium.finance.model.AppUser;
import br.com.veltrium.finance.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {
    private final AppUserRepository userRepository;

    public UserContextService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AppUser currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Usuário autenticado não encontrado", HttpStatus.UNAUTHORIZED));
    }
}
