package ToDoList.ToDoList.dto;

import ToDoList.ToDoList.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto {
    @NotNull(message = "Название задачи не может быть пустым")
    @NotBlank(message = "Название задачи не может содержать только пробелы")
    @Size(min = 3, message = "Название задачи должно содержать минимум 3 символа")
    private String taskName;

    private String taskDescription;

    @NotNull(message = "Срок задачи не может быть пустым")
    @Future(message = "Срок задачи не может быть в прошлом")
    private LocalDate taskDeadline;

    private TaskStatus taskStatus;

    private Long userId;


}
