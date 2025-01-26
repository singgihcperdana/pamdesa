package org.pamdesa.service;

import lombok.RequiredArgsConstructor;
import org.pamdesa.model.entity.User;
import org.pamdesa.payload.response.UserInfoResponse;
import org.pamdesa.repository.UserRepository;
import org.pamdesa.security.services.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserInfoResponse getCurrent() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername()).orElse(new User());
        return UserInfoResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .id(user.getId())
                .roles(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .build();
    }

}
