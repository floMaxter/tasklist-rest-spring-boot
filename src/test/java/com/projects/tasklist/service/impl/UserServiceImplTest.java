package com.projects.tasklist.service.impl;

import com.projects.tasklist.config.TestConfig;
import com.projects.tasklist.domain.exception.ResourceNotFoundException;
import com.projects.tasklist.domain.user.Role;
import com.projects.tasklist.domain.user.User;
import com.projects.tasklist.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getByIdUserIsExistsReturnsNotEmptyUser() {
        // given
        var id = 1L;
        var user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));

        // when
        var testUser = userService.getById(id);

        // then
        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByIdUserIsNotExistsThrowsNotFoundException() {
        // given
        var id = 1L;
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());

        // when

        // then
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(id));
        Mockito.verify(userRepository).findById(id);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getByUsernameUserIsExistsReturnsNotEmptyUser() {
        // given
        var username = "username";
        var user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        // when
        var testUser = userService.getByUsername(username);

        // then
        Mockito.verify(userRepository).findByUsername(username);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByUsernameUserIsNotExistsThrowsNotFoundException() {
        // given
        var username = "username";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // when

        // then
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getByUsername(username));
        Mockito.verify(userRepository).findByUsername(username);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateReturnsUser() {
        // given
        String password = "password";
        var user = new User();
        user.setPassword(password);

        // when
        userService.update(user);

        // then
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
    }

    @Test
    void createUserDoesNotExistReturnsNotEmptyOption() {
        // given
        String username = "username";
        String password = "password";
        var user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // when
        var testUser = userService.create(user);

        // then
        Mockito.verify(userRepository).save(user);
        Mockito.verify(passwordEncoder).encode(password);
        Assertions.assertEquals(Set.of(Role.ROLE_USER), testUser.getRoles());
    }

    @Test
    void createUserUserAlreadyExistsThrowsIllegalStateException() {
        // given
        String username = "username";
        String password = "password";
        var user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User()));

        // when

        // then
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never() ).save(user);
    }

    @Test
    void createUserPasswordConfirmationIsIncorrectThrowsIllegalStateException()
    {
        // given
        String username = "username";
        String password = "password";
        String passwordConfirmation = "passwordConfirmation";
        var user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // when

        // then
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never() ).save(user);
    }

    @Test
    void isTaskOwnerTaskBelongsToUserReturnsBoolean() {
        // given
        var userId = 1L;
        var taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId))
                .thenReturn(true);

        // when
        boolean isOwner = userService.isTaskOwner(userId, taskId);

        // then
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertTrue(isOwner);
    }

    @Test
    void isTaskOwnerTaskDoesNotBelongToUserReturnsBoolean() {
        // given
        var userId = 1L;
        var taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId))
                .thenReturn(false);

        // when
        boolean isOwner = userService.isTaskOwner(userId, taskId);

        // then
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertFalse(isOwner);
    }

    @Test
    void deleteDeletesProduct() {
        // given
        var id = 1L;

        // when
        userService.delete(id);

        // then
        Mockito.verify(userRepository).deleteById(id);
    }
}