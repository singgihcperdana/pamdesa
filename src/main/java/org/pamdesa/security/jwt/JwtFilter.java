package org.pamdesa.security.jwt;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.JsonHelper;
import org.pamdesa.helper.JwtHelper;
import org.pamdesa.model.enums.ErrorCode;
import org.pamdesa.payload.response.Response;
import org.pamdesa.service.ValidTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    private final ValidTokenService tokenService;

    private final UserDetailsService userDetailsService;

    private final JsonHelper jsonHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String[] publicEndpoint = new String[]{"/api/public/", "/v3/api-docs", "/swagger-ui/", "/api/auth/login"};

        for (String excludedUrl : publicEndpoint) {
            if (request.getRequestURI().startsWith(excludedUrl)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        final String token = request.getHeader("token");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new IOException(ErrorCode.INVALID_TOKEN.name());
        }

        final String jwt = token.substring(7);
        final String username = jwtHelper.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (!tokenService.isTokenValid(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Response responseError = Response.builder().code(HttpStatus.UNAUTHORIZED.value())
                        .status(HttpStatus.UNAUTHORIZED.name())
                        .build();
                response.getWriter().write(jsonHelper.toJson(responseError));
                return;
            }

            if (jwtHelper.validateToken(jwt, username)) {
                var userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            }
        }
    }

}
