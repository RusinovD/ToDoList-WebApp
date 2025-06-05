package ToDoList.ToDoList.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.List;

@Data
public class UserDto {
    @NotNull
    private String userName;
    private List<TaskDto> taskList;
}
