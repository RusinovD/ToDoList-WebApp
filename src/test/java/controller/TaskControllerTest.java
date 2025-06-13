package controller;

import ToDoList.ToDoList.controller.TaskController;
import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.entity.Task;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.enums.TaskStatus;
import ToDoList.ToDoList.service.TaskService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private final long CONSTANT_USER_ID = 1L;
    private final long CONSTANT_TASK_ID = 2L;
    private final TaskStatus CONSTANT_TASK_STATUS = TaskStatus.IN_PROGRESS;
    private final String CONSTANT_TASK_NEW_NAME = "TaskNewName";
    private final String CONSTANT_TASK_NEW_DESCRIPTION = "TaskNewDescription";
    private final String CONSTANT_TASK_NEW_STATUS = "DONE";
    private final LocalDate CONSTANT_TASK_NEW_DEADLINE = LocalDate.of(2025, 7,15);

    private final Task task = new Task();

    private final List<TaskDto> taskDtoList = new ArrayList<>();

    private final TaskDto taskDto = new TaskDto();


    @Test
    void addTaskTest() {
        when(taskService.addTask(CONSTANT_USER_ID, taskDto)).thenReturn(task);

        ResponseEntity response = taskController.addTask(CONSTANT_USER_ID, taskDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Задача успешно добавлена!", response.getBody());

        verify(taskService).addTask(CONSTANT_USER_ID, taskDto);
    }

    @Test
    void getAllTasksTest(){
        when(taskService.findAllTasksByUserId(CONSTANT_USER_ID)).thenReturn(taskDtoList);

        ResponseEntity response = taskController.getAllTasks(CONSTANT_USER_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDtoList, response.getBody());

        verify(taskService).findAllTasksByUserId(CONSTANT_USER_ID);
    }

    @Test
    void deleteTaskByIdTest(){
        doNothing().when(taskService).deleteTaskById(CONSTANT_USER_ID, CONSTANT_TASK_ID);

        ResponseEntity response = taskController.deleteTaskById(CONSTANT_USER_ID, CONSTANT_TASK_ID);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Задача успешно удалена!", response.getBody());

        verify(taskService).deleteTaskById(CONSTANT_USER_ID, CONSTANT_TASK_ID);
    }

    @Test
    void filterTasksByStatusTest(){
        when(taskService.findAllByUserIdAndStatus(CONSTANT_USER_ID, CONSTANT_TASK_STATUS)).thenReturn(taskDtoList);

        ResponseEntity response = taskController.filterTasksByStatus(CONSTANT_USER_ID, CONSTANT_TASK_STATUS);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDtoList, response.getBody());

        verify(taskService).findAllByUserIdAndStatus(CONSTANT_USER_ID, CONSTANT_TASK_STATUS);
    }

    @Test
    void changeTaskNameTest(){
        when(taskService.changeTaskName(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_NAME))
                .thenReturn(taskDto);

        ResponseEntity response = taskController
                .changeTaskName(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_NAME);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());

        verify(taskService).changeTaskName(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_NAME);
    }

    @Test
    void changeTaskDescriptionTest(){
        when(taskService.changeTaskDescription(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_DESCRIPTION))
                .thenReturn(taskDto);

        ResponseEntity response = taskController
                .changeTaskDescription(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_DESCRIPTION);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());

        verify(taskService).changeTaskDescription(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_DESCRIPTION);
    }

    @Test
    void changeTaskDeadlineTest(){
        when(taskService.changeTaskDeadline(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_DEADLINE))
                .thenReturn(taskDto);

        ResponseEntity response = taskController
                .changeTaskDeadline(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_DEADLINE);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());

        verify(taskService).changeTaskDeadline(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_DEADLINE);
    }

    @Test
    void changeTaskStatusTest(){
        when(taskService.changeTaskStatus(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_STATUS))
                .thenReturn(taskDto);

        ResponseEntity response = taskController
                .changeTaskStatus(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_STATUS);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taskDto, response.getBody());

        verify(taskService).changeTaskStatus(CONSTANT_USER_ID, CONSTANT_TASK_ID, CONSTANT_TASK_NEW_STATUS);
    }
}
