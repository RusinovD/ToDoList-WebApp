package ToDoList.ToDoList.dto.mappers;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring")
public interface TaskMapper {

    @Mapping(source = "user.id", target = "userId", ignore = true)
    TaskDto toTaskDto(Task task);

    Task toTask (TaskDto taskDto);
}

