package ToDoList.ToDoList.controller;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.enums.TaskStatus;
import ToDoList.ToDoList.exceptions.IllegalStatusFormatException;
import ToDoList.ToDoList.exceptions.TaskNotFoundException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/tasks/{id}")
@AllArgsConstructor
public class TaskController {
    @Autowired
    private final TaskService taskService;

    @PostMapping("/add")
    public ResponseEntity addTask (@PathVariable Long id,
                                   @RequestBody TaskDto taskDto) {
        try {
            taskService.addTask(id, taskDto);
            return ResponseEntity.ok("Задача успешно добавлена!");

        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @GetMapping("/")
    public ResponseEntity getAllTasks (@PathVariable Long id) {
        try {
            return ResponseEntity.ok(taskService.findAllTasksByUserId(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteTaskById (@RequestParam Long taskId) {
        try {
            taskService.deleteTaskById(taskId);
            return ResponseEntity.ok("Задача успешно удалена!");
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @GetMapping("/filter")
    public ResponseEntity filterTasksByStatus (@PathVariable Long id,
                                                   @RequestParam TaskStatus taskStatus) { //
        try {
            return ResponseEntity.ok(taskService.findAllByUserIdAndStatus(id, taskStatus));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @PostMapping("/change-name")
    public ResponseEntity changeTaskName(@RequestParam Long taskId,
                                         @RequestBody String taskName) {
        try {
            return ResponseEntity.ok(taskService.changeTaskName(taskId, taskName));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @PostMapping("/change-description")
    public ResponseEntity changeTaskDescription(@RequestParam Long taskId,
                                                @RequestBody String description) {
        try {
            return ResponseEntity.ok(taskService.changeTaskDescription(taskId, description));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @PostMapping("/change-status")
    public ResponseEntity changeTaskStatus(@RequestParam Long taskId,
                                           @RequestBody String taskStatus) {
        try {
            return ResponseEntity.ok(taskService.changeTaskStatus(taskId, taskStatus));
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }

    @PostMapping("/change-deadline")
    public ResponseEntity changeTaskDeadline(@RequestParam Long taskId,
                                             @RequestBody LocalDate deadline) {
        try {
            return ResponseEntity.ok(taskService.changeTaskDeadline(taskId, deadline));
        } catch (IllegalStatusFormatException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (TaskNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка, попробуйте снова.");
        }
    }
}
