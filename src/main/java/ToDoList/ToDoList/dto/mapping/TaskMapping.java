package ToDoList.ToDoList.dto.mapping;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper (componentModel = "spring")
public interface TaskMapping {

    @Mapping(source = "user.id", target = "userId")
    TaskDto toTaskDto(Task task);

    Task toTask (TaskDto taskDto);

}

