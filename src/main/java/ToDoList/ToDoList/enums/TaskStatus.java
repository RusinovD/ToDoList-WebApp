package ToDoList.ToDoList.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskStatus {
    TODO("TODO"),
    IN_PROGRESS("IN PROGRESS"),
    DONE("DONE");

    private final String status;

    @Override
    public String toString() {
        return status;
    }

    }

