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
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
        User user = userService.checkUserById(userId);
        taskDto.setTaskStatus(TaskStatus.TODO);
        Task task = taskMapping.toTask(taskDto);
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional
    public List<TaskDto> findAllTasksByUserId(Long id) {
        User user = userService.checkUserById(id);
        List<TaskDto> taskDtoList = user.getTaskList().stream().map(taskMapping::toTaskDto).toList();
        if (!taskDtoList.isEmpty()) {
            return taskDtoList;
        } else {
            throw new TaskNotFoundException("Список задач пуст!");
        }
    }

    @Transactional
    public void deleteTaskById(Long userId, Long taskId) {
        User user = userService.checkUserById(userId);
        taskRepository.delete(getTaskById(taskId));
    }

    @Transactional
    public List<TaskDto> findAllByUserIdAndStatus (Long userId, TaskStatus taskStatus) {
        User user = userService.checkUserById(userId);
        List<TaskDto> taskDtoList = user.getTaskList().stream().
                filter(e -> e.getTaskStatus().equals(taskStatus)).
                map(taskMapping::toTaskDto).toList();
        if (!taskDtoList.isEmpty()) {
            return taskDtoList;
        } else {
            throw new TaskNotFoundException("Задач со статусом " + taskStatus + " в списке нет.");
        }
    }

    @Transactional
    public TaskDto changeTaskName(Long userId, Long taskId, String taskName) {
        User user = userService.checkUserById(userId);
        Task task = getTaskById(taskId);
        task.setTaskName(taskName);
        taskRepository.save(task);
        return taskMapping.toTaskDto(task);
    }

    @Transactional
    public TaskDto changeTaskDescription(Long userId, Long taskId, String taskDescription) {
        User user = userService.checkUserById(userId);
        Task task = getTaskById(taskId);
        task.setTaskDescription(taskDescription);
        taskRepository.save(task);
        return taskMapping.toTaskDto(task);
    }

    @Transactional
    public TaskDto changeTaskStatus(Long userId, Long taskId, String taskStatus) {
        User user = userService.checkUserById(userId);
        Task task = getTaskById(taskId);
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
        User user = userService.checkUserById(userId);
        Task task = getTaskById(taskId);
        task.setTaskDeadline(deadline);
        taskRepository.save(task);
        return taskMapping.toTaskDto(task);
    }

    private Task getTaskById (Long taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            return task.get();
        } else {
            throw new TaskNotFoundException("Задача не найдена");
        }
    }
}
