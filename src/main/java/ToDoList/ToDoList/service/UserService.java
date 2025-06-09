package ToDoList.ToDoList.service;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.dto.mapping.UserMapping;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.exceptions.UserAlreadyExistException;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
        if (userRepository.findByUserName(user.getUserName()) != null) {
            throw new UserAlreadyExistException("Пользователь с таким именем уже существует!");
        } else if (userRepository.findByUserEmail(user.getUserEmail()) != null) {
            throw new UserAlreadyExistException("Пользователь с таким email уже существует!");
        }
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser (Long userId) {
        userRepository.delete(checkUserById(userId));
    }

    @Transactional
    @Query("SELECT a FROM users a JOIN FETCH a.tasks")
    public UserDto getUser(Long userId) {
        return userMapping.toDto(checkUserById(userId));
    }

    @Query("SELECT u FROM User u WHERE u.id = :id")
    public User checkUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}
