package ToDoList.ToDoList.controller;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.enums.TaskStatus;
import ToDoList.ToDoList.exceptions.IllegalStatusFormatException;
import ToDoList.ToDoList.exceptions.TaskNotFoundException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/v1/users/{userId}/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {
    private final TaskService taskService;

    @PostMapping("/")
    public ResponseEntity addTask (@PathVariable Long userId,
                                   @RequestBody TaskDto taskDto) {
        try {
            taskService.addTask(userId, taskDto);
            return ResponseEntity.ok("Задача успешно добавлена!");

        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @GetMapping("/")
    public ResponseEntity getAllTasks (@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(taskService.findAllTasksByUserId(userId));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @DeleteMapping("/")
    public ResponseEntity deleteTaskById (@PathVariable Long userId,
                                          @RequestParam Long taskId) {
        try {
            taskService.deleteTaskById(userId, taskId);
            return ResponseEntity.ok("Задача успешно удалена!");
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity filterTasksByStatus (@PathVariable Long userId,
                                               @RequestParam TaskStatus taskStatus) { //
        try {
            return ResponseEntity.ok(taskService.findAllByUserIdAndStatus(userId, taskStatus));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @PatchMapping("/name")
    public ResponseEntity changeTaskName(@PathVariable Long userId,
                                         @RequestParam Long taskId,
                                         @RequestBody String taskName) {
        try {
            return ResponseEntity.ok(taskService.changeTaskName(userId, taskId, taskName));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @PatchMapping("/description")
    public ResponseEntity changeTaskDescription(@PathVariable Long userId,
                                                @RequestParam Long taskId,
                                                @RequestBody String description) {
        try {
            return ResponseEntity.ok(taskService.changeTaskDescription(userId, taskId, description));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @PatchMapping("/status")
    public ResponseEntity changeTaskStatus(@PathVariable Long userId,
                                           @RequestParam Long taskId,
                                           @RequestBody String taskStatus) {
        try {
            return ResponseEntity.ok(taskService.changeTaskStatus(userId, taskId, taskStatus));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @PatchMapping("/deadline")
    public ResponseEntity changeTaskDeadline(@PathVariable Long userId,
                                             @RequestParam Long taskId,
                                             @RequestBody LocalDate deadline) {
        try {
            return ResponseEntity.ok(taskService.changeTaskDeadline(userId, taskId, deadline));
        } catch (IllegalStatusFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }
}
