package org.pamdesa.service;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.ResponseWebHelper;
import org.pamdesa.model.entity.User;
import org.pamdesa.model.payload.response.UserInfoResponse;
import org.pamdesa.repository.UserRepository;
import org.pamdesa.model.UserDetailsImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    return UserDetailsImpl.build(user);
  }

  public UserInfoResponse getCurrent() {
    return ResponseWebHelper.toUserInfoResponse(this.getCurrentUser());
  }

  public User getCurrentUser() {
    UserDetailsImpl userDetails =
        (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return userRepository.findByUsernameFetchData(userDetails.getUsername()).orElse(new User());
  }

}
