package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.handler.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemServiceImplIntegrationTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    private UserDto userDto;
    private ItemDto itemDto;

    private CommentDto commentDto;
    private ItemMapper itemMapper;

    @BeforeEach
    public void setUp() {
        itemDto = new ItemDto(null, "Дрель", "Простая дрель", true, null);
        userDto = new UserDto("user", "user@user.com");
    }

    @Test
    public void shouldCreateTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        ItemDto actualItemDto = itemService.create(itemDto, user.getId());
        assertNotNull(actualItemDto.getId());

        assertEquals(itemDto.getName(), actualItemDto.getName());
        assertEquals(itemDto.getDescription(), actualItemDto.getDescription());
        assertEquals(itemDto.getAvailable(), actualItemDto.getAvailable());
    }

    @Test
    public void shouldUpdateTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        ItemDto createdItemDto = itemService.create(itemDto, user.getId());
        assertNotNull(createdItemDto.getId());

        ItemDto updateItemDto =
                new ItemDto(null, "Дрель", "Простая дрель", false, null);
        ItemDto actualItemDto = itemService.update(updateItemDto,1L, 1L);

        assertEquals(updateItemDto.getName(), actualItemDto.getName());
        assertEquals(updateItemDto.getDescription(), actualItemDto.getDescription());
        assertEquals(updateItemDto.getAvailable(), actualItemDto.getAvailable());
    }

    @Test
    public void shouldGetByIdTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        ItemDto createdItemDto = itemService.create(itemDto, user.getId());
        assertNotNull(createdItemDto.getId());

        ItemDtoWithBooking itemDtoWithBooking = itemService.getById(1L, 1L);
        assertNotNull(itemDtoWithBooking);
    }

    @Test
    public void  shouldGetItemsByOwnerTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        ItemDto createdItemDto = itemService.create(itemDto, user.getId());
        assertNotNull(createdItemDto.getId());

        ItemDto itemDto2 = new ItemDto(null, "Дрель2", "Простая дрель2", true, null);
        ItemDto createdItemDto2 = itemService.create(itemDto2, user.getId());
        assertNotNull(createdItemDto2.getId());

        List<ItemDtoWithBooking> actualItems = itemService.getItemsByOwner(1L);
        assertNotNull(actualItems);
        assertEquals(2, actualItems.size());
    }

    @Test
    public void shouldGetItemsBySearchTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        ItemDto createdItemDto = itemService.create(itemDto, user.getId());
        assertNotNull(createdItemDto.getId());

        ItemDto itemDto2 = new ItemDto(null, "Дрель2", "Простая дрель2", true, null);
        ItemDto createdItemDto2 = itemService.create(itemDto2, user.getId());
        assertNotNull(createdItemDto2.getId());

        List<ItemDto> expectedItems = List.of(createdItemDto, createdItemDto2);

        List<ItemDto> actualItems = itemService.getItemsBySearch("Дрель");
        assertNotNull(actualItems);
        assertEquals(2, actualItems.size());
        assertEquals(expectedItems, actualItems);
    }

    @Test
    public void shouldAddCommentWithValidationExceptionTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        ItemDto createdItemDto = itemService.create(itemDto, user.getId());
        assertNotNull(createdItemDto.getId());

        commentDto = new CommentDto(1L,"some text",new Item(),"user", LocalDateTime.now());

        assertThrows(ValidationException.class, () -> {
            itemService.addComment(1L,1L, commentDto);
        });
    }

    @Test
    public void shouldAddComment() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());
        UserDto userDto2 = new UserDto("user2", "user2@user.com");
        User user2 = userService.create(userDto2);
        assertNotNull(user2.getId());

        ItemDto createdItemDto = itemService.create(itemDto, user.getId());
        assertNotNull(createdItemDto.getId());

        Booking booking = new Booking(1L,LocalDateTime.now().minusDays(4),LocalDateTime.now().minusDays(3),
                new Item(1L, "Дрель", "Простая дрель", true,1L, null)
                ,user2,BookingStatus.APPROVED);
        bookingRepository.save(booking);

        commentDto = new CommentDto(2L,"some text",new Item(),"user", LocalDateTime.now());

        Comment comment = itemService.addComment(2L, 1L, commentDto);
    }

    @Test
    public void shouldGetItemIfExistsTest() {
        User user = userService.create(userDto);
        assertNotNull(user.getId());

        itemService.create(itemDto, user.getId());

        Item item = itemService.getItemIfExists(1L);
        assertNotNull(item.getId());

        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());

        itemMapper = new ItemMapper(new BookingMapper());
        itemMapper.toItemShortDto(item);
    }
}