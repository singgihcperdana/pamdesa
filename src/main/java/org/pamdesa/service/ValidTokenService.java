package org.pamdesa.service;

import lombok.RequiredArgsConstructor;
import org.pamdesa.model.entity.User;
import org.pamdesa.model.entity.ValidToken;
import org.pamdesa.repository.ValidTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidTokenService {

    private final ValidTokenRepository validTokenRepository;

    public void saveToken(String token, LocalDateTime expirationTime, User user) {
        ValidToken validToken = new ValidToken();
        validToken.setToken(token);
        validToken.setExpirationTime(expirationTime);
        validToken.setUser(user);
        validTokenRepository.save(validToken);
    }

    public boolean isTokenValid(String token) {
        return validTokenRepository.findByToken(token).isPresent();
    }

    public void cleanUpExpiredTokens() {
        validTokenRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

    @Transactional
    public void deleteByToken(String token){
        validTokenRepository.deleteByToken(token);
    }
}