package ToDoList.ToDoList.dto;

import ToDoList.ToDoList.enums.TaskStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskDto {
    private String taskName;
    private String taskDescription;
    private LocalDate taskDeadline;
    private TaskStatus taskStatus;
    private Long userId;


}
