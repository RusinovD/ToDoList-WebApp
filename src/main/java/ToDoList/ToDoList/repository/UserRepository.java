package ToDoList.ToDoList.repository;

import ToDoList.ToDoList.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {
    User findByUserName(String userName);

    User findByUserEmail(String userEmail);

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, value = "users_entity-graph")
    Optional<User> findById(Long userId);
}

