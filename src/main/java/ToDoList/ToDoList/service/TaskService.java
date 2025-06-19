package ToDoList.ToDoList.service;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.dto.mappers.TaskMapper;
import ToDoList.ToDoList.entity.Task;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.enums.TaskStatus;
import ToDoList.ToDoList.exceptions.IllegalStatusFormatException;
import ToDoList.ToDoList.exceptions.TaskNotFoundException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.repository.TaskRepository;
import ToDoList.ToDoList.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public Task addTask (Long userId, TaskDto taskDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        taskDto.setTaskStatus(TaskStatus.TODO);
        Task task = taskMapper.toTask(taskDto);
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional
    public List<TaskDto> findAllTasksByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return user.getTaskList().stream().map(taskMapper::toTaskDto).toList();
    }

    @Transactional
    public void deleteTaskById(Long userId, Long taskId) {
        taskRepository.delete(taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Задача не найдена")));
    }

    @Transactional
    public List<TaskDto> findAllByUserIdAndStatus (Long userId, TaskStatus taskStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        return user.getTaskList().stream().
                filter(e -> e.getTaskStatus().equals(taskStatus)).
                map(taskMapper::toTaskDto).toList();
    }

    @Transactional
    public TaskDto changeTaskName(Long userId, Long taskId, String taskName) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
        task.setTaskName(taskName);
        taskRepository.save(task);
        return taskMapper.toTaskDto(task);
    }

    @Transactional
    public TaskDto changeTaskDescription(Long userId, Long taskId, String taskDescription) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
        task.setTaskDescription(taskDescription);
        taskRepository.save(task);
        return taskMapper.toTaskDto(task);
    }

    @Transactional
    public TaskDto changeTaskStatus(Long userId, Long taskId, String taskStatus) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
        try {
            task.setTaskStatus(TaskStatus.valueOf(taskStatus));
            taskRepository.save(task);
            return taskMapper.toTaskDto(task);
        } catch (IllegalArgumentException e) {
            throw new IllegalStatusFormatException("Неверный формат статуса");
        }
    }

    @Transactional
    public TaskDto changeTaskDeadline(Long userId, Long taskId, LocalDate deadline) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Задача не найдена"));
        task.setTaskDeadline(deadline);
        taskRepository.save(task);
        return taskMapper.toTaskDto(task);
    }
}
