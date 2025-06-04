package ToDoList.ToDoList.service;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.dto.mapping.UserMapping;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.exceptions.UserAlreadyExistException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapping userMapping;

    @Transactional
    public User registration (User user) {
        if (userRepository.findByUserName(user.getUserName()) != null
                || userRepository.findByUserName(user.getUserEmail()) != null) {
            throw new UserAlreadyExistException("Пользователь с таким именем уже существует!");
        } else if (userRepository.findByUserEmail(user.getUserEmail()) != null) {
            throw new UserAlreadyExistException("Пользователь с таким email уже существует!");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser (Long userId) {
        userRepository.delete(getUserById(userId));
    }

    @Transactional
    @Query("SELECT a FROM users a JOIN FETCH a.tasks")
    public UserDto getUser(Long userId) {
        return userMapping.toDto(getUserById(userId));
    }

    protected User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
