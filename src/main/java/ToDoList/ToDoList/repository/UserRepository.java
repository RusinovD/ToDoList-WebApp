package ToDoList.ToDoList.repository;

import ToDoList.ToDoList.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    User findByUserName(String userName);

    User findByUserEmail(String userEmail);

    User findUserById (Long id);

    User getUserById(Long userId);
}
