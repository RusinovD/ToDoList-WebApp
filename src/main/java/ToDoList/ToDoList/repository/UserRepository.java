package ToDoList.ToDoList.repository;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    User findByUserName(String userName);

    User findByUserEmail(String userEmail);

}

