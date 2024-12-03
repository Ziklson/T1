package com.kuvarin.taskcrud.exception;


public class TasksNotFoundException extends RuntimeException {
    public TasksNotFoundException (String message) {
        super(message);
    }
}
