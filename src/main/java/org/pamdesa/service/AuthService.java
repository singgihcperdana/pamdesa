package org.pamdesa.service;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.JwtHelper;
import org.pamdesa.model.entity.Organization;
import org.pamdesa.model.entity.User;
import org.pamdesa.model.enums.ErrorCode;
import org.pamdesa.model.enums.UserRole;
import org.pamdesa.model.error.ClientException;
import org.pamdesa.model.payload.request.LoginRequest;
import org.pamdesa.model.payload.request.SignupRequest;
import org.pamdesa.repository.OrganizationRepository;
import org.pamdesa.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthenticationManager authenticationManager;

  private final JwtHelper jwtHelper;

  private final UserRepository userRepository;

  private final OrganizationRepository organizationRepository;

  private final ValidTokenService validTokenService;

  public String login(LoginRequest request) {
    Authentication authenticate = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
    String token = jwtHelper.generateToken(request.getUsername());
    LocalDateTime expirationTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(jwtHelper.getTokenExpiration(token)),
            ZoneId.systemDefault());
    SecurityContextHolder.getContext().setAuthentication(authenticate);

    UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
    userRepository.findByUsername(userDetails.getUsername())
        .ifPresent(user -> validTokenService.saveToken(token, expirationTime, user));
    return token;
  }

  public void logout(String token) {
    String cleanToken = token.replace("Bearer ", "");
    validTokenService.deleteByToken(cleanToken);
  }

  public User signup(SignupRequest request) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    Organization organization = organizationRepository.findById(request.getOrganizationId())
        .orElseThrow(() -> new ClientException(ErrorCode.DATA_NOT_FOUND));
    return userRepository.save(User.builder()
        .email(request.getEmail())
            .active(Boolean.FALSE)
            .username(request.getUsername())
            .password(encoder.encode(request.getPassword()))
            .fullName(request.getFullName())
            .phoneNumber(request.getPhoneNumber())
            .userRole(UserRole.CUSTOMER)
            .organization(organization)
            .address(request.getAddress())
        .build());
  }

}
