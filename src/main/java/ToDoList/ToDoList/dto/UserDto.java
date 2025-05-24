package ToDoList.ToDoList.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private String userName;
    private List<TaskDto> taskList;
}
