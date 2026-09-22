package com.company.ecommerce_api.features.profile;

import com.company.ecommerce_api.shared.security.UserAccount;
import com.company.ecommerce_api.shared.security.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProfileService {

    private final UserAccountRepository userAccountRepository;

    public ProfileService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public ProfileResponse getProfile(Authentication authentication) {
        return toResponse(findAccount(authentication));
    }

    @Transactional
    public ProfileResponse updateProfile(Authentication authentication, ProfileRequest request) {
        UserAccount account = findAccount(authentication);
        account.updateProfile(request.firstName().trim(), request.lastName().trim());
        return toResponse(account);
    }

    private UserAccount findAccount(Authentication authentication) {
        return userAccountRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    private ProfileResponse toResponse(UserAccount account) {
        return new ProfileResponse(
                account.getUsername(),
                account.getFirstName(),
                account.getLastName(),
                account.getAuthorities().stream().findFirst().orElseThrow().getAuthority()
        );
    }
}