package ToDoList.ToDoList.service;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.dto.mapping.TaskMapping;
import ToDoList.ToDoList.entity.Task;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.enums.TaskStatus;
import ToDoList.ToDoList.exceptions.IllegalStatusFormatException;
import ToDoList.ToDoList.exceptions.TaskNotFoundException;
import ToDoList.ToDoList.repository.TaskRepository;
import ToDoList.ToDoList.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserService userService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapping taskMapping;

    @Transactional
    public Task addTask (Long userId, TaskDto taskDto) {
        User user = userService.findUserById(userId);
        taskDto.setTaskStatus(TaskStatus.TODO);
        Task task = taskMapping.toTask(taskDto);
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional
    public List<TaskDto> findAllTasksByUserId(Long id) {
        User user = userService.findUserById(id);
        return user.getTaskList().stream().map(taskMapping::toTaskDto).toList();
    }

    @Transactional
    public void deleteTaskById(Long userId, Long taskId) {
        User user = userService.findUserById(userId);
        taskRepository.delete(findTaskById(taskId));
    }

    @Transactional
    public List<TaskDto> findAllByUserIdAndStatus (Long userId, TaskStatus taskStatus) {
        User user = userService.findUserById(userId);
        return user.getTaskList().stream().
                filter(e -> e.getTaskStatus().equals(taskStatus)).
                map(taskMapping::toTaskDto).toList();
    }

    @Transactional
    public TaskDto changeTaskName(Long userId, Long taskId, String taskName) {
        User user = userService.findUserById(userId);
        Task task = findTaskById(taskId);
        task.setTaskName(taskName);
        taskRepository.save(task);
        return taskMapping.toTaskDto(task);
    }

    @Transactional
    public TaskDto changeTaskDescription(Long userId, Long taskId, String taskDescription) {
        User user = userService.findUserById(userId);
        Task task = findTaskById(taskId);
        task.setTaskDescription(taskDescription);
        taskRepository.save(task);
        return taskMapping.toTaskDto(task);
    }

    @Transactional
    public TaskDto changeTaskStatus(Long userId, Long taskId, String taskStatus) {
        User user = userService.findUserById(userId);
        Task task = findTaskById(taskId);
        try {
            task.setTaskStatus(TaskStatus.valueOf(taskStatus));
            taskRepository.save(task);
            return taskMapping.toTaskDto(task);
        } catch (IllegalArgumentException e) {
            throw new IllegalStatusFormatException("Неверный формат статуса");
        }
    }

    @Transactional
    public TaskDto changeTaskDeadline(Long userId, Long taskId, LocalDate deadline) {
        User user = userService.findUserById(userId);
        Task task = findTaskById(taskId);
        task.setTaskDeadline(deadline);
        taskRepository.save(task);
        return taskMapping.toTaskDto(task);
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
    }
}
