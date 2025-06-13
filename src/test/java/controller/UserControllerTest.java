package controller;

import ToDoList.ToDoList.controller.UserController;
import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.service.UserService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final long CONSTANT_USER_ID = 1L;

    final UserDto userDto = mock(UserDto.class);
    final User user = mock(User.class);

    @BeforeEach
    void setUp(){
        user.setId(CONSTANT_USER_ID);
    }

    @Test
    void registrationTest(){
        when(userService.registration(userDto)).thenReturn(user);

        ResponseEntity response = userController.registration(userDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Регистрация прошла успешно", response.getBody());

        verify(userService).registration(userDto);
    }

    @Test
    void deleteUserTest(){
        doNothing().when(userService).deleteUser(CONSTANT_USER_ID);

        ResponseEntity response = userController.deleteUser(CONSTANT_USER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Пользователь удален", response.getBody());

        verify(userService).deleteUser(CONSTANT_USER_ID);
    }

    @Test
    void getOneUserTest() {
        when(userService.getUser(CONSTANT_USER_ID)).thenReturn(userDto);

        ResponseEntity response = userController.getOneUser(CONSTANT_USER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDto, response.getBody());

        verify(userService).getUser(CONSTANT_USER_ID);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOneUser (@PathVariable Long id) {
        UserDto userDto = userService.getUser(id);
        return ResponseEntity.ok(userDto);
    }
}
