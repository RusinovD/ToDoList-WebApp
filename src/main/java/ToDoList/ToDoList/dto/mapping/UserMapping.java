package ToDoList.ToDoList.dto.mapping;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = TaskMapping.class)
public interface UserMapping {
    UserDto toDto (User user);

    User toUser (UserDto userDto);
}
