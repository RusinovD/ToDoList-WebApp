package ToDoList.ToDoList.dto.mappers;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface UserMapper {
    @Mapping(target = "userEmail", expression = "java(shouldIncludeEmail() ? user.getUserEmail() : null)")
    UserDto toUserDto(User user);

    User toUser (UserDto userDto);

    default boolean shouldIncludeEmail() {
        return false;
    }
}
