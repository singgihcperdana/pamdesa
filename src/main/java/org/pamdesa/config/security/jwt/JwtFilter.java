package org.pamdesa.config.security.jwt;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.JsonHelper;
import org.pamdesa.helper.JwtHelper;
import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.model.constant.AppPath;
import org.pamdesa.model.enums.ErrorCode;
import org.pamdesa.model.enums.UserRole;
import org.pamdesa.properties.AccessRulesProperties;
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
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    private final ValidTokenService tokenService;

    private final UserDetailsService userDetailsService;

    private final JsonHelper jsonHelper;

//    private final RoleEndpointRepository roleEndpointRepository;

    private final AccessRulesProperties accessRulesProperties;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

//    private final String[] publicPaths = new String[]{"/api/public/", "/v3/api-docs", "/swagger-ui/", AppPath.LOGIN};

//    private final String[] nonAuthorizedPaths = new String[]{AppPath.CURRENT, AppPath.LOGOUT};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        // Check if the path is public
        if (isPublicPath(requestPath)) {
            filterChain.doFilter(request, response);
            return;
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

                if(!isAuthorizedPath(userDetails, requestPath, requestMethod)) {
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

    private boolean isAuthorizedPath(UserDetails userDetails, String requestPath, String requestMethod) {
        boolean isAuthedPath = accessRulesProperties.getAuthedPaths().stream()
                .anyMatch(noAuthorizedPath -> pathMatcher.match(noAuthorizedPath, requestPath));
        if(isAuthedPath) {
            return true;
        }
        Map<String, Map<String, List<String>>> accessRules = accessRulesProperties.getRules();
        List<UserRole> userRoles = userDetails.getAuthorities().stream()
                .map(role -> UserRole.valueOf(role.getAuthority()) )
                .toList();
        return userRoles.stream()
                .anyMatch(role-> isPathAndMethodAllowed(role.name(), requestPath, requestMethod, accessRules));
    }

    private boolean isPathAndMethodAllowed(String role, String path, String method, Map<String, Map<String, List<String>>> accessRules) {
        Map<String, List<String>> roleRules = accessRules.get(role);
        if (roleRules != null) {
            return roleRules.entrySet()
                    .stream()
                    .anyMatch(entry -> pathMatcher.match(entry.getKey(), path) &&
                            entry.getValue().contains(method));
        }
        return false;
    }

    private boolean isPublicPath(String path) {
        List<String> publicPaths = accessRulesProperties.getPublicPaths();
        return publicPaths.stream().anyMatch(publicPath -> pathMatcher.match(publicPath, path));
    }

}
