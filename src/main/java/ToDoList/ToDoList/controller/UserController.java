package ToDoList.ToDoList.controller;


import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.exceptions.UserAlreadyExistException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity registration (@RequestBody User user) {
        try {
            userService.registration(user);
            return ResponseEntity.ok("Регистрация прошла успешно");
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser (@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok("Пользователь успешно удален");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getOneUser (@PathVariable Long id) {
        try {
            UserDto userDto = userService.getUser(id);
            if (!userDto.getTaskList().isEmpty()) {
                return ResponseEntity.ok(userDto);
            } else {
                return ResponseEntity.ok(userDto.getUserName() + "\n список задач пуст.");
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }
}
