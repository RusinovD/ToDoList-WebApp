package service;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.dto.mapping.TaskMapping;
import ToDoList.ToDoList.entity.Task;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.enums.TaskStatus;
import ToDoList.ToDoList.exceptions.TaskNotFoundException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.repository.TaskRepository;
import ToDoList.ToDoList.repository.UserRepository;
import ToDoList.ToDoList.service.TaskService;
import ToDoList.ToDoList.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskMapping taskMapping;
    @Mock
    private UserService userService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserRepository userRepository;

    @Spy
    @InjectMocks
    private TaskService taskService;

    private final long CONSTANT_ID = 1L;
    private final long CONSTANT_TASK_ID = 2L;

    @Test
    void addTaskTest_UserFound () {
        TaskDto taskDto = new TaskDto();
        Task task = new Task();
        User user = new User();

        doReturn(user).when(userService).checkUserById(CONSTANT_ID);
        when(taskMapping.toTask(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);

        Task addedTask = taskService.addTask(CONSTANT_ID, taskDto);

        assertNotNull(addedTask);
        assertEquals(addedTask, task);

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping).toTask(taskDto);
        verify(taskRepository).save(task);
    }

    @Test
    void addTaskTest_UserNotFound () {
        when(userService.checkUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTask(any());
    }

    @Test
    void findAllTasksByUserIdTest_UserFound() {
        User user = new User();
        user.setId(CONSTANT_ID);

        Task task1 = new Task();
        task1.setTaskName("Task 1");
        Task task2 = new Task();
        task2.setTaskName("Task 2");
        List<Task> taskList = Arrays.asList(task1, task2);
        user.setTaskList(taskList);

        TaskDto taskDto1 = new TaskDto();
        taskDto1.setTaskName("Task 1 DTO");
        TaskDto taskDto2 = new TaskDto();
        taskDto2.setTaskName("Task 2 DTO");
        List<TaskDto> taskDtoList = Arrays.asList(taskDto1, taskDto2);

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);

        when(taskMapping.toTaskDto(task1)).thenReturn(taskDto1);
        when(taskMapping.toTaskDto(task2)).thenReturn(taskDto2);

        List<TaskDto> actualTaskDtoList = taskService.findAllTasksByUserId(CONSTANT_ID);

        assertNotNull(actualTaskDtoList);
        assertEquals(taskDtoList.size(), actualTaskDtoList.size());
        assertEquals(taskDtoList.get(0).getTaskName(), actualTaskDtoList.get(0).getTaskName());
        assertEquals(taskDtoList.get(1).getTaskName(), actualTaskDtoList.get(1).getTaskName());
    }

    @Test
    void findAllTasksByUserIdTest_UserNotFound() {
        when(userService.checkUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void deleteTaskByIdTest_UserFound_TaskFound() {
        User user = new User();
        user.setId(CONSTANT_ID);
        Task task = new Task();
        task.setId(CONSTANT_ID);

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_ID)).thenReturn(Optional.of(task));

        assertDoesNotThrow(() -> taskService.deleteTaskById(CONSTANT_ID, CONSTANT_TASK_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTaskByIdTest_UserFound_TaskNotFound() {
        User user = new User();
        user.setId(CONSTANT_ID);
        Task task = new Task();

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,() -> taskService.deleteTaskById(CONSTANT_ID, CONSTANT_TASK_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository, never()).delete(task);
    }

    @Test
    void deleteTaskByIdTest_UserNotFound() {
        when(userService.checkUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void findAllByUserIdAndStatusTest_UserNotFound() {
        when(userService.checkUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void findAllByUserIdAndStatusTest_UserFound_TaskListIsEmpty() {
        User user = new User();
        user.setId(CONSTANT_ID);

        Task task1 = new Task();
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
        Task task2 = new Task();
        task2.setTaskStatus(TaskStatus.IN_PROGRESS);
        Task task3 = new Task();
        task3.setTaskStatus(TaskStatus.DONE);
        List<Task> taskList = List.of(task1, task2, task3);
        user.setTaskList(taskList);

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);

        List<TaskDto> actualTaskDtoList1 = taskService.findAllByUserIdAndStatus(CONSTANT_ID, TaskStatus.TODO);

        assertTrue(actualTaskDtoList1.isEmpty());

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void findAllByUserIdAndStatusTest_UserFound_TaskList() {
        User user = new User();
        user.setId(CONSTANT_ID);

        Task task1 = new Task();
        task1.setId(1L);
        task1.setTaskStatus(TaskStatus.IN_PROGRESS);
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTaskStatus(TaskStatus.IN_PROGRESS);
        Task task3 = new Task();
        task3.setId(3L);
        task3.setTaskStatus(TaskStatus.DONE);
        List<Task> taskList = List.of(task1, task2, task3);
        user.setTaskList(taskList);

        TaskDto taskDto1 = new TaskDto();
        taskDto1.setTaskStatus(TaskStatus.IN_PROGRESS);
        TaskDto taskDto2 = new TaskDto();
        taskDto2.setTaskStatus(TaskStatus.IN_PROGRESS);

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskMapping.toTaskDto(task1)).thenReturn(taskDto1);
        when(taskMapping.toTaskDto(task2)).thenReturn(taskDto2);

        List<TaskDto> actualTaskDtoList2 = taskService.findAllByUserIdAndStatus(CONSTANT_ID, TaskStatus.IN_PROGRESS);

        for (TaskDto taskDto : actualTaskDtoList2) {
            assertEquals(TaskStatus.IN_PROGRESS, taskDto.getTaskStatus());
        }

        verify(taskMapping).toTaskDto(task1);
        verify(taskMapping).toTaskDto(task2);

    }

    @Test
    void changeTaskNameTest_UserNotFound(){
        when(userService.checkUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void changeTaskNameTest_UserFound_TaskNotFound(){
        User user = new User();
        user.setId(CONSTANT_ID);
        Task task = new Task();
        task.setTaskName("oldname");

        String newTaskName = "newname";

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.changeTaskName(CONSTANT_ID, CONSTANT_TASK_ID, newTaskName));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository, never()).save(any());
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void changeTaskNameTest_UserFound_TaskFound(){
        User user = new User();
        user.setId(CONSTANT_ID);

        String newTaskName = "newname";

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskName(newTaskName);

        Task task = new Task();
        task.setTaskName("oldname");

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapping.toTaskDto(any(Task.class))).thenReturn(taskDto);

        TaskDto actualTaskDto = taskService.changeTaskName(CONSTANT_ID, CONSTANT_TASK_ID, newTaskName);

        assertNotNull(actualTaskDto);

        assertEquals(newTaskName, actualTaskDto.getTaskName());

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository).save(task);
        verify(taskMapping).toTaskDto(task);
    }

    @Test
    void changeTaskDescriptionTest_UserNotFound(){
        when(userService.checkUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void changeTaskDescriptionTest_UserFound_TaskNotFound(){
        User user = new User();
        user.setId(CONSTANT_ID);
        Task task = new Task();
        task.setTaskDescription("oldDescription");

        String newTaskDescription = "newDescription";

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.changeTaskDescription(CONSTANT_ID, CONSTANT_TASK_ID, newTaskDescription));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository, never()).save(any());
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void changeTaskDescriptionTest_UserFound_TaskFound(){
        User user = new User();
        user.setId(CONSTANT_ID);

        String newTaskDescription = "newDescription";

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskDescription(newTaskDescription);

        Task task = new Task();
        task.setTaskDescription("oldDescription");

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapping.toTaskDto(any(Task.class))).thenReturn(taskDto);

        TaskDto actualTaskDto = taskService.changeTaskDescription(CONSTANT_ID, CONSTANT_TASK_ID, newTaskDescription);

        assertNotNull(actualTaskDto);

        assertEquals(newTaskDescription, actualTaskDto.getTaskDescription());

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository).save(task);
        verify(taskMapping).toTaskDto(task);
    }

    @Test
    void changeTaskStatusTest_UserNotFound(){
        when(userService.checkUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void changeTaskStatusTest_UserFound_TaskNotFound(){
        User user = new User();
        user.setId(CONSTANT_ID);
        Task task = new Task();
        task.setTaskStatus(TaskStatus.TODO);

        String newTaskStatus = "IN_PROGRESS";

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.changeTaskStatus(CONSTANT_ID, CONSTANT_TASK_ID, newTaskStatus));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository, never()).save(any());
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void changeTaskStatusTest_UserFound_TaskFound(){
        User user = new User();
        user.setId(CONSTANT_ID);

        String newTaskStatus = "IN_PROGRESS";
        TaskStatus taskStatusNew = TaskStatus.IN_PROGRESS;

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskStatus(taskStatusNew);

        Task task = new Task();
        task.setTaskStatus(TaskStatus.TODO);

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapping.toTaskDto(any(Task.class))).thenReturn(taskDto);

        TaskDto actualTaskDto = taskService.changeTaskStatus(CONSTANT_ID, CONSTANT_TASK_ID, newTaskStatus);

        assertNotNull(actualTaskDto);

        assertEquals(taskStatusNew, actualTaskDto.getTaskStatus());

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository).save(task);
        verify(taskMapping).toTaskDto(task);
    }

    @Test
    void changeTaskDeadlineTest_UserNotFound(){
        when(userService.checkUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.checkUserById(CONSTANT_ID));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void changeTaskDeadlineTest_UserFound_TaskNotFound(){
        User user = new User();
        user.setId(CONSTANT_ID);
        Task task = new Task();
        task.setTaskDeadline(LocalDate.of(2025, 6,15));

        LocalDate newTaskDeadline = LocalDate.of(2025, 6,20);

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.changeTaskDeadline(CONSTANT_ID, CONSTANT_TASK_ID, newTaskDeadline));

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository, never()).save(any());
        verify(taskMapping, never()).toTaskDto(any());
    }

    @Test
    void changeTaskDeadlineTest_UserFound_TaskFound(){
        User user = new User();
        user.setId(CONSTANT_ID);

        LocalDate newTaskDeadline = (LocalDate.of(2025, 6,15));;

        TaskDto taskDto = new TaskDto();
        taskDto.setTaskDeadline(newTaskDeadline);

        Task task = new Task();
        task.setTaskDeadline(LocalDate.of(2025, 6,15));

        when(userService.checkUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.of(task));
        when(taskMapping.toTaskDto(any(Task.class))).thenReturn(taskDto);

        TaskDto actualTaskDto = taskService.changeTaskDeadline(CONSTANT_ID, CONSTANT_TASK_ID, newTaskDeadline);

        assertNotNull(actualTaskDto);

        assertEquals(newTaskDeadline, actualTaskDto.getTaskDeadline());

        verify(userService).checkUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID);
        verify(taskRepository).save(task);
        verify(taskMapping).toTaskDto(task);
    }

    @Test
    void getTaskByIdTest_TaskFound(){
        Task task = new Task();
        task.setId(CONSTANT_TASK_ID);

        when(taskRepository.findById(CONSTANT_TASK_ID)).thenReturn(Optional.of(task));

        Task actualTask = taskService.getTaskById(CONSTANT_TASK_ID);

        assertNotNull(actualTask);

        verify(taskRepository).findById(CONSTANT_TASK_ID);

    }

    @Test
    void getTaskByIdTest_TaskNotFound(){
        when(taskRepository.findById(CONSTANT_TASK_ID)).thenThrow(new TaskNotFoundException("Задача не найдена"));

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(CONSTANT_TASK_ID));

        verify(taskService).getTaskById(CONSTANT_ID);

    }

}
