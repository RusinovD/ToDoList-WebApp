package ToDoList.ToDoList.controller;


import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity registration (@Valid @RequestBody User user) {
        userService.registration(user);
        return ResponseEntity.ok("Регистрация прошла успешно");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser (@PathVariable Long id) {
        userService.deleteUser(id);
            return ResponseEntity.ok("Пользователь успешно удален");
    }

    @GetMapping("/{id}")
    public ResponseEntity getOneUser (@PathVariable Long id) {
        UserDto userDto = userService.getUser(id);
        if (!userDto.getTaskList().isEmpty()) {
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.ok(userDto.getUserName() + "\n список задач пуст.");
        }
    }
}
