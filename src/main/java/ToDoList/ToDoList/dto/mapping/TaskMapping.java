package ToDoList.ToDoList.dto.mapping;

import ToDoList.ToDoList.dto.TaskDto;
import ToDoList.ToDoList.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper (componentModel = "spring")
public interface TaskMapping {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "taskDeadline", target = "taskDeadline", dateFormat = "yyyy-MM-dd")
    TaskDto toTaskDto(Task task);

    @Mapping(target = "taskDeadline", expression = "java(parseDate(taskDto.getTaskDeadline()))")
    Task toTask (TaskDto taskDto);

    default LocalDate parseDate(String dateString) {
        if (dateString == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }
}

