package ToDoList.ToDoList.exceptions;

public class IllegalStatusFormatException extends RuntimeException {
    public IllegalStatusFormatException(String message) {
        super(message);
    }
}
