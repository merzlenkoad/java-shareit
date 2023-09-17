package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplUnitTest {
    private RequestService requestService;

    @Mock
    private UserService userService;

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        requestService = new RequestServiceImpl(requestRepository,
                new RequestMapper(itemRepository,new ItemMapper(new BookingMapper())), userService);
    }

    @Test
    void getRequestByIdWithWrongId() {
        when(userService.getUserIfExists(anyLong())).thenReturn(null);
        assertThrows(NotFoundException.class, () -> {
            requestService.getRequestById(1L,1L);
        });
    }
}