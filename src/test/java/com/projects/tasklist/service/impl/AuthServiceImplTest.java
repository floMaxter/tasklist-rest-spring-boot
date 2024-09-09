package com.projects.tasklist.service.impl;

import com.projects.tasklist.config.TestConfig;
import com.projects.tasklist.domain.exception.ResourceNotFoundException;
import com.projects.tasklist.domain.user.Role;
import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.web.dto.auth.JwtRequest;
import com.projects.tasklist.web.dto.auth.JwtResponse;
import com.projects.tasklist.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserServiceImpl userService;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthServiceImpl authService;

    @Test
    void loginRequestWithCorrectUsernameAndPasswordReturnsAuthenticationToken() {
        // given
        Long userId = 1L;
        String username = "username";
        String password = "password";
        Set<Role> roles = Collections.emptySet();
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";

        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);

        var user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRoles(roles);

        Mockito.when(userService.getByUsername(username))
                .thenReturn(user);
        Mockito.when(tokenProvider.createAccessToken(userId, username, roles))
                .thenReturn(accessToken);
        Mockito.when(tokenProvider.createRefreshToken(userId, username))
                .thenReturn(refreshToken);

        // when
        JwtResponse response = authService.login(request);

        // then
        Mockito.verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword())
        );
        Mockito.verify(userService).getByUsername(username);
        Assertions.assertEquals(username, response.getUsername());
        Assertions.assertEquals(userId, response.getId());
        Assertions.assertNotNull(response.getAccessToken());
        Assertions.assertNotNull(response.getRefreshToken());
    }

    @Test
    void loginWithInvalidUsernameThrowsResourceNotFoundException() {
        // given
        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);

        Mockito.when(userService.getByUsername(username))
                .thenThrow(ResourceNotFoundException.class);
        // when

        // then
        Mockito.verifyNoInteractions(tokenProvider);
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> authService.login(request));
    }

    @Test
    void loginWithInvalidPasswordThrowsResourceNotFoundException() {
        // given
        String username = "username";
        String password = "password";
        JwtRequest request = new JwtRequest();
        request.setUsername(username);
        request.setPassword(password);

        Mockito.when(authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                )
        ).thenThrow(new BadCredentialsException("Invalid credentials"));

        // when

        // then
        Mockito.verifyNoInteractions(tokenProvider);
        Assertions.assertThrows(BadCredentialsException.class,
                () -> authService.login(request));
    }

    @Test
    void refresh() {
        // given
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        String newRefreshToken = "newRefreshToken";
        JwtResponse response = new JwtResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(newRefreshToken);

        Mockito.when(tokenProvider.refreshUserTokens(refreshToken))
                .thenReturn(response);

        // when
        JwtResponse testResponse = authService.refresh(refreshToken);

        // then
        Mockito.verify(tokenProvider).refreshUserTokens(refreshToken);
        Assertions.assertEquals(response, testResponse);
    }
}
