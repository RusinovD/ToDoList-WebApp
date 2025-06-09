package ToDoList.ToDoList.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.List;

@Data
public class UserDto {
    @NotNull(message = "Имя пользователя не может быть пустым")
    @NotBlank(message = "Имя пользователя не может содержать только пробелы")
    @Size(min = 3, message = "Имя пользователя должно содержать минимум 3 символа")
    private String userName;

    @Email(message = "Неверный формат email")
    private String userEmail;

    private List<TaskDto> taskList;
}
