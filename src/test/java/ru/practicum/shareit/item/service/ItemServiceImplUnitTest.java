package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.handler.exception.NotOwnerException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BookingStatus;

import javax.persistence.Column;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplUnitTest {

    private ItemService itemService;
    @Mock
    private UserService userService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;


    @BeforeEach
    public void setUp() {
        itemService = new ItemServiceImpl(
                new ItemMapper(new BookingMapper()),
                new CommentMapper(),
                itemRepository,
                bookingRepository,
                commentRepository,
                userService);
    }

    @Test
    void updateWithOwnerIdNotEqualsUserId() {
        ItemDto itemDto = new ItemDto(1L, "Дрель", "Простая дрель", true,null);
        Item item = new Item(1L, "Дрель", "Простая дрель", true, 1L, null);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(NotOwnerException.class, () -> {
            itemService.update(itemDto,1L, 2L);
        });
    }

    @Test
    void updateWithNameAndDescriptionAndAvailableBlank() {
        ItemDto itemDto = new ItemDto(1L, null, null, null,null);
        Item item = new Item(1L, "Дрель", "Простая дрель", true, 1L, null);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto actualItemDto = itemService.update(itemDto, 1L, 1L);
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());

        assertEquals(itemDto, actualItemDto);
    }

    @Test
    void getItemsBySearchWithTextIsBlank() {
       List<ItemDto> itemsDto = itemService.getItemsBySearch("");
       assertEquals(new ArrayList<>(), itemsDto);
    }

    @Test
    void getItemIfExists() {
        assertThrows(NotFoundException.class, () -> {
            itemService.getItemIfExists(anyLong());
        });
    }
}