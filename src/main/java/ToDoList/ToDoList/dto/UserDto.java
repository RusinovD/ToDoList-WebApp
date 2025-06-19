package ToDoList.ToDoList.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    @NotNull(message = "Имя пользователя не может быть пустым")
    @NotBlank(message = "Имя пользователя не может содержать только пробелы")
    @Size(min = 3, message = "Имя пользователя должно содержать минимум 3 символа")
    private String userName;

    @Email(message = "Неверный формат email")
    private String userEmail;

    private List<TaskDto> taskList;
}
