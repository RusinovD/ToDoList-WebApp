package ToDoList.ToDoList.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStatusFormatException.class)
    public ResponseEntity<String> handleIllegalStatusFormatException(IllegalStatusFormatException e) {
        return new ResponseEntity<> (e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException e) {
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<String> handleUserAlreadyExistException(UserAlreadyExistException e) {
        return new ResponseEntity<> (e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = "Неверный формат данных";
        if (e.getBindingResult().hasErrors()) {
            errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }
        return new ResponseError (errorMessage);
    }
}
