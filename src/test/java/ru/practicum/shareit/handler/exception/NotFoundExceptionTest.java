package ru.practicum.shareit.handler.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {
    private NotFoundException notFoundException;

    @Test
    void getId() {
        notFoundException = new NotFoundException("some text", 1L);
        assertEquals(1L,notFoundException.getId());
    }
}