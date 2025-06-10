package service;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.dto.mapping.UserMapping;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.exceptions.UserAlreadyExistException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.repository.UserRepository;
import ToDoList.ToDoList.service.UserService;
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

    @Test
    void getUserTest_UserFound() {
        final User user = mock(User.class);
        final UserDto userDto = mock(UserDto.class);

        doReturn(user).when(userService).checkUserById(CONSTANT_ID);
        when(userMapping.toUserDto(user)).thenReturn(userDto); // Это правильно, так как userMapping - @Mock

        final UserDto result = userService.getUser(CONSTANT_ID);

        assertNotNull(result);
        assertEquals(userDto, result);

        verify(userService).checkUserById(CONSTANT_ID);
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
    void checkUserByIdTest_UserFound() {
        final User user = mock(User.class);
        when(userRepository.findById(CONSTANT_ID)).thenReturn(java.util.Optional.of(user));

        final User actual = userService.checkUserById(CONSTANT_ID);
        assertNotNull(actual);
        assertEquals(user, actual);
        verify(userRepository).findById(CONSTANT_ID);
    }

    @Test
    void checkUserByIdTest_UserNotFound() {
        when(userRepository.findById(CONSTANT_ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));
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
        User user = new User();
        user.setId(CONSTANT_ID);
        when(userRepository.findById(CONSTANT_ID)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.deleteUser(CONSTANT_ID));

        verify(userRepository).findById(CONSTANT_ID);
        verify(userRepository).delete(user);
    }

    @Test
    void registration_UserNameAlreadyExists() {
        UserDto userDto = new UserDto();
        userDto.setUserName("existingUser");

        when(userRepository.findByUserName("existingUser")).thenReturn(new User());

        assertThrows(UserAlreadyExistException.class, () -> userService.registration(userDto));

        verify(userRepository).findByUserName("existingUser");
        verify(userRepository, never()).findByUserEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registration_UserEmailAlreadyExists() {
        UserDto userDto = new UserDto();
        userDto.setUserName("newUser");
        userDto.setUserEmail("existingEmail@example.com");

        when(userRepository.findByUserName("newUser")).thenReturn(null);
        when(userRepository.findByUserEmail("existingEmail@example.com")).thenReturn(new User());

        assertThrows(UserAlreadyExistException.class, () -> userService.registration(userDto));

        verify(userRepository).findByUserName("newUser");
        verify(userRepository).findByUserEmail("existingEmail@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registration_SuccessfulRegistration() {

        UserDto userDto = new UserDto();
        userDto.setUserName("newUser");
        userDto.setUserEmail("newEmail@example.com");

        User user = new User();
        user.setUserName("newUser");
        user.setUserEmail("newEmail@example.com");

        when(userRepository.findByUserName("newUser")).thenReturn(null);
        when(userRepository.findByUserEmail("newEmail@example.com")).thenReturn(null);
        when(userMapping.toUser(userDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registration(userDto);

        assertNotNull(registeredUser);
        assertEquals("newUser", registeredUser.getUserName());
        assertEquals("newEmail@example.com", registeredUser.getUserEmail());

        verify(userRepository).findByUserName("newUser");
        verify(userRepository).findByUserEmail("newEmail@example.com");
        verify(userMapping).toUser(userDto);
        verify(userRepository).save(user);
    }
}



