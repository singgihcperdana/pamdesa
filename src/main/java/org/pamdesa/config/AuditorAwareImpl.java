package org.pamdesa.config;

import lombok.extern.slf4j.Slf4j;
import org.pamdesa.config.security.auth.UserDetailsImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.of(userDetails.getUsername());
        } catch (Exception e) {
            log.warn("getCurrentAuditor failed, fallback to SYSTEM: {}", e.getMessage());
            return Optional.of("SYSTEM");
        }
    }
}