package com.templlo.service.reservation.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.templlo.service.reservation.global.GlobalConst.*;

@Slf4j
@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final String[] EXCLUDE_PATH_PREFIXES = {"/api/checker/health-check", "/api/test/reservation-exception"};

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String curPath = request.getRequestURI();
        return Arrays.stream(EXCLUDE_PATH_PREFIXES).anyMatch(curPath::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader(HEADER_NAME_USER_ID);
        String userRole = request.getHeader(HEADER_NAME_USER_ROLE);
        String usedAuthToken = request.getHeader(HEADER_NAME_USED_AUTH_TOKEN);

        checkUserInfoValid(userId, userRole, usedAuthToken);

        List<GrantedAuthority> authorityList = toAuthorityList(userRole);
        PreAuthenticatedAuthenticationToken authenticationToken =
                new PreAuthenticatedAuthenticationToken(userId, usedAuthToken, authorityList);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }


    private List<GrantedAuthority> toAuthorityList(String userRole) {
        return List.of(new SimpleGrantedAuthority(userRole));
    }

    private void checkUserInfoValid(String userId, String userRole, String usedAuthToken) {
        if (userId == null || userRole == null || userId.isBlank() || userRole.isBlank() || usedAuthToken == null || usedAuthToken.isBlank()) {
            throw new AuthException(AuthStatusCode.USER_INFO_REQUIRED);
        }

        if (Arrays.stream(UserRole.values()).noneMatch(v -> v.name().equals(userRole))) {
            throw new AuthException(AuthStatusCode.USER_ROLE_INVALID);
        }
    }
}
