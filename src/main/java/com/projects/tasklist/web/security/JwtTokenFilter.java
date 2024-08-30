package com.projects.tasklist.web.security;

import com.projects.tasklist.domain.exception.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @SneakyThrows
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain) {
        String bearerToken =
                ((HttpServletRequest) request).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        if (bearerToken != null
                && jwtTokenProvider.validateToken(bearerToken)) {
            try {
                Authentication authentication
                        = jwtTokenProvider.getAuthentication(bearerToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                }
            } catch (ResourceNotFoundException ignored) {
            }
        }
        chain.doFilter(request, response);
    }
}
