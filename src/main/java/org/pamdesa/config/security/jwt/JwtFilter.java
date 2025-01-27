package org.pamdesa.config.security.jwt;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.JsonHelper;
import org.pamdesa.helper.JwtHelper;
import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.model.enums.ErrorCode;
import org.pamdesa.model.enums.UserRole;
import org.pamdesa.repository.RoleEndpointRepository;
import org.pamdesa.service.ValidTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    private final ValidTokenService tokenService;

    private final UserDetailsService userDetailsService;

    private final JsonHelper jsonHelper;

    private final RoleEndpointRepository roleEndpointRepository;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private final String[] publicPaths = new String[]{"/api/public/", "/v3/api-docs", "/swagger-ui/", "/api/auth/login"};

    private final List<String> noAuthorizedPaths = List.of("/api/auth/current");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        for (String excludedUrl : publicPaths) {
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
                response.getWriter().write(jsonHelper.toJson(ResponseHelper.status(HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED.name())));
                return;
            }

            if (jwtHelper.validateToken(jwt, username)) {
                var userDetails = userDetailsService.loadUserByUsername(username);

                if(!isAuthorizedPath(userDetails, request.getServletPath(), request.getMethod())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write(jsonHelper.toJson(ResponseHelper.status(HttpStatus.FORBIDDEN.value(),
                            HttpStatus.FORBIDDEN.name())));
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                filterChain.doFilter(request, response);
            }
        }
    }

    boolean isAuthorizedPath(UserDetails userDetails, String requestPath, String requestMethod) {
        boolean isNonAuthorizedPath = noAuthorizedPaths.stream()
                .anyMatch(noAuthorizedPath -> pathMatcher.match(noAuthorizedPath , requestPath));
        if(isNonAuthorizedPath) {
            return true;
        }

        List<UserRole> userRoles = userDetails.getAuthorities().stream()
                .map(role -> UserRole.valueOf(role.getAuthority()) )
                .toList();

        return roleEndpointRepository.findPathsByRoleIn(userRoles).stream()
                .anyMatch(allowedPath -> {
                    if (pathMatcher.match(allowedPath.getPath(), requestPath)) {
                        return allowedPath.getMethod().equalsIgnoreCase(requestMethod);
                    }
                    return false;
                });
    }

}
