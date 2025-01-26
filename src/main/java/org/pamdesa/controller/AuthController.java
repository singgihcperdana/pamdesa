package org.pamdesa.controller;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.payload.request.LoginRequest;
import org.pamdesa.payload.response.Response;
import org.pamdesa.helper.JwtHelper;
import org.pamdesa.payload.response.UserInfoResponse;
import org.pamdesa.repository.UserRepository;
import org.pamdesa.service.UserService;
import org.pamdesa.service.ValidTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtHelper jwtHelper;

    private final ValidTokenService tokenService;

    private final UserRepository userRepository;

    private final ValidTokenService validTokenService;

    private final UserService userService;


    @PostMapping("/login")
    public Response<String> login(@Validated @RequestBody LoginRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
            String token = jwtHelper.generateToken(request.getUsername());
            LocalDateTime expirationTime = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(jwtHelper.getTokenExpiration(token)), ZoneId.systemDefault());

            userRepository.findByUsername(userDetails.getUsername())
                    .ifPresent(user -> tokenService.saveToken(token, expirationTime, user));

            return ResponseHelper.ok(token);
        } catch (BadCredentialsException ex) {
            return ResponseHelper.status(400, "BadCredential");
        }
    }

    @GetMapping("current")
    public Response<UserInfoResponse> current() {
        return ResponseHelper.ok(userService.getCurrent());
    }

    @PostMapping("/logout")
    public Response<String> logout(@RequestHeader("token") String token) {
        String cleanToken = token.replace("Bearer ", "");
        validTokenService.deleteByToken(cleanToken);
        return ResponseHelper.ok();
    }

}
