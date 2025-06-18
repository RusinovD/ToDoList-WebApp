package service;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.dto.mapping.UserMapping;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.exceptions.UserAlreadyExistException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.repository.UserRepository;
import ToDoList.ToDoList.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapping userMapping;

    @InjectMocks
    @Spy
    UserService userService;

    private static final long CONSTANT_ID = 1L;

    private final String CONSTANT_USER_NAME = "User";
    private final String CONSTANT_USER_EMAIL ="test@example.com";

    final User user = new User();
    final UserDto userDto = new UserDto();

    @BeforeEach
    void setUp() {
        user.setId(CONSTANT_ID);
        user.setUserName(CONSTANT_USER_NAME);
        user.setUserEmail(CONSTANT_USER_EMAIL);
        userDto.setUserName(CONSTANT_USER_NAME);
        userDto.setUserEmail(CONSTANT_USER_EMAIL);
    }

    @Test
    void getUserTest_UserFound() {
        doReturn(user).when(userService).findUserById(CONSTANT_ID);
        when(userMapping.toUserDto(user)).thenReturn(userDto);

        final UserDto result = userService.getUser(CONSTANT_ID);

        assertNotNull(result);
        assertEquals(userDto, result);

        verify(userService).findUserById(CONSTANT_ID);
        verify(userMapping).toUserDto(user);
    }

    @Test
    void getUserTest_UserNotFound() {
        when(userRepository.findById(CONSTANT_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUser(CONSTANT_ID));

        verify(userRepository).findById(CONSTANT_ID);
        verify(userMapping, never()).toUserDto(any());
    }

    @Test
    void findUserByIdTest_UserFound() {
        when(userRepository.findById(CONSTANT_ID)).thenReturn(java.util.Optional.of(user));

        final User actual = userService.findUserById(CONSTANT_ID);

        assertNotNull(actual);
        assertEquals(user, actual);

        verify(userRepository).findById(CONSTANT_ID);
    }

    @Test
    void findUserByIdTest_UserNotFound() {
        when(userRepository.findById(CONSTANT_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));
    }

    @Test
    void  deleteUserTest_UserNotFound() {
        when(userRepository.findById(CONSTANT_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(CONSTANT_ID));

        verify(userRepository).findById(CONSTANT_ID);
        verify(userMapping, never()).toUserDto(any());
    }

    @Test
    void deleteUserTest_UserFound() {
        when(userRepository.findById(CONSTANT_ID)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(CONSTANT_ID));

        verify(userRepository).findById(CONSTANT_ID);
        verify(userRepository).delete(user);
    }

    @Test
    void registration_UserNameAlreadyExists() {
        when(userRepository.findByUserName(CONSTANT_USER_NAME)).thenReturn(new User());

        assertThrows(UserAlreadyExistException.class, () -> userService.registration(userDto));

        verify(userRepository).findByUserName(CONSTANT_USER_NAME);
        verify(userRepository, never()).findByUserEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registration_UserEmailAlreadyExists() {
        when(userRepository.findByUserName(CONSTANT_USER_NAME)).thenReturn(null);
        when(userRepository.findByUserEmail(CONSTANT_USER_EMAIL)).thenReturn(new User());

        assertThrows(UserAlreadyExistException.class, () -> userService.registration(userDto));

        verify(userRepository).findByUserName(CONSTANT_USER_NAME);
        verify(userRepository).findByUserEmail(CONSTANT_USER_EMAIL);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registration_SuccessfulRegistration() {
        when(userRepository.findByUserName(CONSTANT_USER_NAME)).thenReturn(null);
        when(userRepository.findByUserEmail(CONSTANT_USER_EMAIL)).thenReturn(null);
        when(userMapping.toUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registration(userDto);

        assertNotNull(registeredUser);
        assertEquals(CONSTANT_USER_NAME, registeredUser.getUserName());
        assertEquals(CONSTANT_USER_EMAIL, registeredUser.getUserEmail());

        verify(userRepository).findByUserName(CONSTANT_USER_NAME);
        verify(userRepository).findByUserEmail(CONSTANT_USER_EMAIL);
        verify(userMapping).toUser(userDto);
        verify(userRepository).save(user);
    }
}



