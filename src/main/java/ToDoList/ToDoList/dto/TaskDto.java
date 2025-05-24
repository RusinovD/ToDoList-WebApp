package ToDoList.ToDoList.dto;

import ToDoList.ToDoList.enums.TaskStatus;
import lombok.Data;

@Data
public class TaskDto {
    private String taskName;
    private String taskDescription;
    private String taskDeadline;
    private TaskStatus taskStatus;
    private Long userId;


}
