package ToDoList.ToDoList.dto.mapping;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TaskMapping.class)
public interface UserMapping {
    @Mapping(target = "userEmail", expression = "java(shouldIncludeEmail() ? user.getUserEmail() : null)")
    UserDto toDto (User user);

    User toUser (UserDto userDto);

    default boolean shouldIncludeEmail() {
        return false;
    }
}
