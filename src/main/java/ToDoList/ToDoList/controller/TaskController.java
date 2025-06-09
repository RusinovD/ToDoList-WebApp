package ToDoList.ToDoList.controller;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.enums.TaskStatus;
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
        taskService.addTask(userId, taskDto);
        return ResponseEntity.ok("Задача успешно добавлена!");
    }

    @GetMapping("/")
    public ResponseEntity getAllTasks (@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.findAllTasksByUserId(userId));
    }

    @DeleteMapping("/")
    public ResponseEntity deleteTaskById (@PathVariable Long userId,
                                          @RequestParam Long taskId) {
        taskService.deleteTaskById(userId, taskId);
            return ResponseEntity.ok("Задача успешно удалена!");
    }

    @GetMapping("/filter")
    public ResponseEntity filterTasksByStatus (@PathVariable Long userId,
                                               @RequestParam TaskStatus taskStatus) {
        return ResponseEntity.ok(taskService.findAllByUserIdAndStatus(userId, taskStatus));
    }

    @PatchMapping("/name")
    public ResponseEntity changeTaskName(@PathVariable Long userId,
                                         @RequestParam Long taskId,
                                         @RequestBody String taskName) {
        return ResponseEntity.ok(taskService.changeTaskName(userId, taskId, taskName));
    }

    @PatchMapping("/description")
    public ResponseEntity changeTaskDescription(@PathVariable Long userId,
                                                @RequestParam Long taskId,
                                                @RequestBody String description) {
        return ResponseEntity.ok(taskService.changeTaskDescription(userId, taskId, description));
    }

    @PatchMapping("/status")
    public ResponseEntity changeTaskStatus(@PathVariable Long userId,
                                           @RequestParam Long taskId,
                                           @RequestBody String taskStatus) {
        return ResponseEntity.ok(taskService.changeTaskStatus(userId, taskId, taskStatus));
    }

    @PatchMapping("/deadline")
    public ResponseEntity changeTaskDeadline(@PathVariable Long userId,
                                             @RequestParam Long taskId,
                                             @RequestBody LocalDate deadline) {
        return ResponseEntity.ok(taskService.changeTaskDeadline(userId, taskId, deadline));
    }
}
