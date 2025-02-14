package org.pamdesa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.pamdesa.config.filter.AuthTokenFilter;
import org.pamdesa.model.payload.response.Response;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder,
      UserDetailsService userDetailsService) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder).and().build();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(ObjectMapper objectMapper,
      AuthTokenFilter authTokenFilter, HttpSecurity http) throws Exception {
    http.csrf().disable().sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
          response.setContentType(MediaType.APPLICATION_JSON_VALUE);
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          objectMapper.writeValue(response.getOutputStream(),
              Response.builder().status(HttpStatus.UNAUTHORIZED.name())
                  .code(HttpStatus.UNAUTHORIZED.value()).build());
        });
    return http.build();
  }

}


