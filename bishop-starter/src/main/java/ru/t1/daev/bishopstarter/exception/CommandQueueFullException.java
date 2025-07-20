package ru.t1.daev.bishopstarter.exception;

public class CommandQueueFullException extends RuntimeException {
    public CommandQueueFullException(String message) {
        super(message);
    }
}