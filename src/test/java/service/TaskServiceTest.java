package service;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.dto.mappers.TaskMapper;
import ToDoList.ToDoList.entity.Task;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.enums.TaskStatus;
import ToDoList.ToDoList.exceptions.TaskNotFoundException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.repository.TaskRepository;
import ToDoList.ToDoList.repository.UserRepository;
import ToDoList.ToDoList.service.TaskService;
import ToDoList.ToDoList.service.UserService;
import org.junit.jupiter.api.BeforeEach;
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
    private TaskMapper taskMapper;
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
    private final long CONSTANT_TASK_ID1 = 2L;
    private final long CONSTANT_TASK_ID2 = 3L;
    private final long CONSTANT_TASK_ID3 = 4L;

    User user = new User();

    TaskDto taskDto1 = new TaskDto();
    TaskDto taskDto2 = new TaskDto();
    TaskDto taskDto3 = new TaskDto();
    List<TaskDto> taskDtoList = Arrays.asList(taskDto1, taskDto2, taskDto3);
    private final TaskStatus CONSTANT_TASK_DTO_NEW_TASK_STATUS = TaskStatus.IN_PROGRESS;

    Task task1 = new Task();
    Task task2 = new Task();
    Task task3 = new Task();
    List<Task> taskList = Arrays.asList(task1, task2);

    private final String CONSTANT_TASK_DTO_NAME1 = "TaskDTO1";
    private final String CONSTANT_TASK_DTO_NAME2 = "TaskDTO1";

    private final String CONSTANT_TASK_NAME1 = "Task1";
    private final String CONSTANT_TASK_NAME2 = "Task1";
    private final String CONSTANT_NEW_TASK_NAME = "NEW Task Name";

    private final String CONSTANT_TASK_DESCRIPTION1 = "Task Description1";
    private final String CONSTANT_TASK_NEW_DESCRIPTION = "New Task Description1";

    private final TaskStatus CONSTANT_TASK_STATUS1 = TaskStatus.TODO;
    private final TaskStatus CONSTANT_TASK_STATUS2 = TaskStatus.IN_PROGRESS;
    private final TaskStatus CONSTANT_TASK_STATUS3 = TaskStatus.DONE;
    private final String CONSTANT_TASK_NEW_STATUS = "IN_PROGRESS";

    private final LocalDate CONSTANT_TASK_DEADLINE1 = LocalDate.of(2025, 6,15);
    private final LocalDate CONSTANT_TASK_NEW_DEADLINE = LocalDate.of(2025, 7,15);

    @BeforeEach
    void setUp(){
        user.setId(CONSTANT_ID);
        user.setTaskList(taskList);

        task1.setId(CONSTANT_TASK_ID1);
        task1.setTaskName(CONSTANT_TASK_NAME1);
        task1.setTaskStatus(CONSTANT_TASK_STATUS2);
        task1.setTaskDescription(CONSTANT_TASK_DESCRIPTION1);

        task2.setId(CONSTANT_TASK_ID2);
        task2.setTaskName(CONSTANT_TASK_NAME2);
        task2.setTaskStatus(CONSTANT_TASK_STATUS2);

        task3.setId(CONSTANT_TASK_ID3);
        task3.setTaskStatus(CONSTANT_TASK_STATUS3);

        List<Task> taskList = List.of(task1, task2, task3);
        user.setTaskList(taskList);

        taskDto1.setTaskName(CONSTANT_TASK_DTO_NAME1);
        taskDto1.setTaskStatus(CONSTANT_TASK_STATUS2);

        taskDto2.setTaskName(CONSTANT_TASK_DTO_NAME2);
        taskDto2.setTaskStatus(CONSTANT_TASK_STATUS2);
    }

    @Test
    void addTaskTest_UserFound () {
        doReturn(user).when(userService).findUserById(CONSTANT_ID);
        when(taskMapper.toTask(taskDto1)).thenReturn(task1);
        when(taskRepository.save(task1)).thenReturn(task1);

        Task addedTask = taskService.addTask(CONSTANT_ID, taskDto1);

        assertNotNull(addedTask);
        assertEquals(addedTask, task1);

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper).toTask(taskDto1);
        verify(taskRepository).save(task1);
    }

    @Test
    void addTaskTest_UserNotFound () {
        when(userService.findUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTask(any());
    }

    @Test
    void findAllTasksByUserIdTest_UserFound() {
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);

        when(taskMapper.toTaskDto(task1)).thenReturn(taskDto1);
        when(taskMapper.toTaskDto(task2)).thenReturn(taskDto2);
        when(taskMapper.toTaskDto(task3)).thenReturn(taskDto3);

        List<TaskDto> actualTaskDtoList = taskService.findAllTasksByUserId(CONSTANT_ID);

        assertNotNull(actualTaskDtoList);
        assertEquals(taskDtoList.size(), actualTaskDtoList.size());
        for (int i = 0; i < taskDtoList.size(); i++) {
            assertEquals(taskDtoList.get(i).getTaskName(), actualTaskDtoList.get(i).getTaskName());
        }
    }

    @Test
    void findAllTasksByUserIdTest_UserNotFound() {
        when(userService.findUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void deleteTaskByIdTest_UserFound_TaskFound() {
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.of(task1));

        assertDoesNotThrow(() -> taskService.deleteTaskById(CONSTANT_ID, CONSTANT_TASK_ID1));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository).delete(task1);
    }

    @Test
    void deleteTaskByIdTest_UserFound_TaskNotFound() {
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,() -> taskService.deleteTaskById(CONSTANT_ID, CONSTANT_TASK_ID1));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository, never()).delete(task1);
    }

    @Test
    void deleteTaskByIdTest_UserNotFound() {
        when(userService.findUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void findAllByUserIdAndStatusTest_UserNotFound() {
        when(userService.findUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void findAllByUserIdAndStatusTest_UserFound_TaskListIsEmpty() {
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);

        List<TaskDto> actualTaskDtoList1 = taskService.findAllByUserIdAndStatus(CONSTANT_ID, TaskStatus.TODO);

        assertTrue(actualTaskDtoList1.isEmpty());

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void findAllByUserIdAndStatusTest_UserFound_TaskList() {
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskMapper.toTaskDto(task1)).thenReturn(taskDto1);
        when(taskMapper.toTaskDto(task2)).thenReturn(taskDto2);

        List<TaskDto> actualTaskDtoList2 = taskService.findAllByUserIdAndStatus(CONSTANT_ID, CONSTANT_TASK_STATUS2);

        for (TaskDto taskDto : actualTaskDtoList2) {
            assertEquals(CONSTANT_TASK_STATUS2, taskDto.getTaskStatus());
        }

        verify(taskMapper).toTaskDto(task1);
        verify(taskMapper).toTaskDto(task2);
    }

    @Test
    void changeTaskNameTest_UserNotFound(){
        when(userService.findUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void changeTaskNameTest_UserFound_TaskNotFound(){
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.changeTaskName(CONSTANT_ID, CONSTANT_TASK_ID1, CONSTANT_NEW_TASK_NAME));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository, never()).save(any());
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void changeTaskNameTest_UserFound_TaskFound(){
        taskDto1.setTaskName(CONSTANT_NEW_TASK_NAME);
        task1.setTaskName(CONSTANT_TASK_NAME1);

        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.of(task1));
        when(taskMapper.toTaskDto(any(Task.class))).thenReturn(taskDto1);

        TaskDto actualTaskDto = taskService.changeTaskName(CONSTANT_ID, CONSTANT_TASK_ID1, CONSTANT_NEW_TASK_NAME);

        assertNotNull(actualTaskDto);

        assertEquals(CONSTANT_NEW_TASK_NAME, actualTaskDto.getTaskName());

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository).save(task1);
        verify(taskMapper).toTaskDto(task1);
    }

    @Test
    void changeTaskDescriptionTest_UserNotFound(){
        when(userService.findUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void changeTaskDescriptionTest_UserFound_TaskNotFound(){
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.changeTaskDescription(CONSTANT_ID, CONSTANT_TASK_ID1, CONSTANT_TASK_NEW_DESCRIPTION));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository, never()).save(any());
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void changeTaskDescriptionTest_UserFound_TaskFound(){
        taskDto1.setTaskDescription(CONSTANT_TASK_NEW_DESCRIPTION);
        task1.setTaskDescription(CONSTANT_TASK_DESCRIPTION1);

        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.of(task1));
        when(taskMapper.toTaskDto(any(Task.class))).thenReturn(taskDto1);

        TaskDto actualTaskDto =
                taskService.changeTaskDescription(CONSTANT_ID, CONSTANT_TASK_ID1, CONSTANT_TASK_NEW_DESCRIPTION);

        assertNotNull(actualTaskDto);

        assertEquals(CONSTANT_TASK_NEW_DESCRIPTION, actualTaskDto.getTaskDescription());

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository).save(task1);
        verify(taskMapper).toTaskDto(task1);
    }

    @Test
    void changeTaskStatusTest_UserNotFound(){
        when(userService.findUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void changeTaskStatusTest_UserFound_TaskNotFound(){
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.changeTaskStatus(CONSTANT_ID, CONSTANT_TASK_ID1, CONSTANT_TASK_NEW_STATUS));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository, never()).save(any());
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void changeTaskStatusTest_UserFound_TaskFound(){
        taskDto1.setTaskStatus(CONSTANT_TASK_DTO_NEW_TASK_STATUS);

        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.of(task1));
        when(taskMapper.toTaskDto(any(Task.class))).thenReturn(taskDto1);

        TaskDto actualTaskDto = taskService.changeTaskStatus(CONSTANT_ID, CONSTANT_TASK_ID1, CONSTANT_TASK_NEW_STATUS);

        assertNotNull(actualTaskDto);

        assertEquals(CONSTANT_TASK_DTO_NEW_TASK_STATUS, actualTaskDto.getTaskStatus());

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository).save(task1);
        verify(taskMapper).toTaskDto(task1);
    }

    @Test
    void changeTaskDeadlineTest_UserNotFound(){
        when(userService.findUserById(CONSTANT_ID)).thenThrow(new UserNotFoundException("Пользователь не найден"));

        assertThrows(UserNotFoundException.class, () -> userService.findUserById(CONSTANT_ID));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void changeTaskDeadlineTest_UserFound_TaskNotFound(){
        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.changeTaskDeadline(CONSTANT_ID, CONSTANT_TASK_ID1, CONSTANT_TASK_NEW_DEADLINE));

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository, never()).save(any());
        verify(taskMapper, never()).toTaskDto(any());
    }

    @Test
    void changeTaskDeadlineTest_UserFound_TaskFound(){
        taskDto1.setTaskDeadline(CONSTANT_TASK_NEW_DEADLINE);

        when(userService.findUserById(CONSTANT_ID)).thenReturn(user);
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.of(task1));
        when(taskMapper.toTaskDto(any(Task.class))).thenReturn(taskDto1);

        TaskDto actualTaskDto = taskService.changeTaskDeadline(CONSTANT_ID, CONSTANT_TASK_ID1, CONSTANT_TASK_NEW_DEADLINE);

        assertNotNull(actualTaskDto);

        assertEquals(CONSTANT_TASK_NEW_DEADLINE, actualTaskDto.getTaskDeadline());

        verify(userService).findUserById(CONSTANT_ID);
        verify(taskRepository).findById(CONSTANT_TASK_ID1);
        verify(taskRepository).save(task1);
        verify(taskMapper).toTaskDto(task1);
    }

    @Test
    void findTaskByIdTest_TaskFound(){
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenReturn(Optional.of(task1));

        Task actualTask = taskService.findTaskById(CONSTANT_TASK_ID1);

        assertNotNull(actualTask);

        verify(taskRepository).findById(CONSTANT_TASK_ID1);
    }

    @Test
    void findTaskByIdTest_TaskNotFound(){
        when(taskRepository.findById(CONSTANT_TASK_ID1)).thenThrow(new TaskNotFoundException("Задача не найдена"));

        assertThrows(TaskNotFoundException.class, () -> taskService.findTaskById(CONSTANT_TASK_ID1));

        verify(taskService).findTaskById(CONSTANT_TASK_ID1);
    }
}
