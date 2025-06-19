package ToDoList.ToDoList.service;

import ToDoList.ToDoList.dto.UserDto;
import ToDoList.ToDoList.dto.mapping.UserMapping;
import ToDoList.ToDoList.entity.User;
import ToDoList.ToDoList.exceptions.UserNotFoundException;
import ToDoList.ToDoList.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapping userMapping;

    @Transactional
    public User registration (UserDto userDto) {
        return userRepository.save(userMapping.toUser(userDto));
    }

    @Transactional
    public void deleteUser (Long userId) {
        userRepository.delete(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")));
    }

    @Transactional
    public UserDto getUser(Long userId) {
        return userMapping.toUserDto(userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден")));
    }
}
