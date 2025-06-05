package ToDoList.ToDoList.dto;

import ToDoList.ToDoList.enums.TaskStatus;
import lombok.Data;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Data
public class TaskDto {
    @NotNull
    private String taskName;
    @NotNull
    private String taskDescription;
    @NotNull
    private LocalDate taskDeadline;
    private TaskStatus taskStatus;
    private Long userId;


}
