package ru.practicum.shareit.handler.exception;

public class NotFoundException extends RuntimeException {

    final Long id;

    public NotFoundException(String message, Long id) {
        super(message);
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
