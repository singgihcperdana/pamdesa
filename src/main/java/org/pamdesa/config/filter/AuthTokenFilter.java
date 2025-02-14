package org.pamdesa.config.filter;

import lombok.RequiredArgsConstructor;
import org.pamdesa.helper.JsonHelper;
import org.pamdesa.helper.JwtHelper;
import org.pamdesa.helper.ResponseHelper;
import org.pamdesa.model.enums.ErrorCode;
import org.pamdesa.model.properties.AccessRulesProperties;
import org.pamdesa.service.UserService;
import org.pamdesa.service.ValidTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

  private final JwtHelper jwtHelper;

  private final ValidTokenService tokenService;

  private final UserService userService;

  private final JsonHelper jsonHelper;

  private final AccessRulesProperties accessRulesProperties;

  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String requestPath = request.getServletPath();
    String requestMethod = request.getMethod();

    // Skip filter for public paths
    if (this.isValidPath(requestPath, requestMethod, accessRulesProperties.getPublicPaths())) {
      filterChain.doFilter(request, response);
      return;
    }

    final String token = request.getHeader("token");

    if (token == null || !token.startsWith("Bearer ")) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write(jsonHelper.toJson(
          ResponseHelper.status(HttpStatus.BAD_REQUEST.value(), ErrorCode.INVALID_TOKEN.name())));
      return;
    }

    String jwt = token.substring(7);
    String username = jwtHelper.extractUsername(jwt);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      if (!tokenService.isTokenValid(jwt)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonHelper.toJson(
            ResponseHelper.status(HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name())));
        return;
      }

      if (jwtHelper.validateToken(jwt, username)) {
        var userDetails = userService.loadUserByUsername(username);

        if (!isAuthorizedPath(userDetails, requestPath, requestMethod)) {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          response.getWriter().write(jsonHelper.toJson(
              ResponseHelper.status(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN.name())));
          return;
        }

        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);
      }
    }
  }

  private boolean isAuthorizedPath(UserDetails userDetails, String requestPath,
      String requestMethod) {
    //skip filter for non specific role
    if (this.isValidPath(requestPath, requestMethod, accessRulesProperties.getAuthedPaths())) {
      return true;
    }
    return userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(
        role -> this.isValidPath(requestPath, requestMethod,
            accessRulesProperties.getRules().get(role)));
  }

  private boolean isValidPath(String requestPath, String requestMethod,
      List<AccessRulesProperties.Path> paths) {
    if (CollectionUtils.isEmpty(paths)) {
      return false;
    }
    return paths.stream().anyMatch(authedPath -> {
      if (pathMatcher.match(authedPath.getPath(), requestPath)) {
        List<String> methods =
            Optional.ofNullable(authedPath.getMethods()).filter(data -> !data.isEmpty())
                .orElse(List.of("*"));
        return methods.contains("*") || methods.contains(requestMethod);
      }
      return false;
    });
  }

}
