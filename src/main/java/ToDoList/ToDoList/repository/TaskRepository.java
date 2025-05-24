package ToDoList.ToDoList.repository;

import ToDoList.ToDoList.entity.Task;
import ToDoList.ToDoList.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository <Task, Long> {
}
