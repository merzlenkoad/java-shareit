package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BookingStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private BookingDto bookingDto;
    private UserDto userDto;
    private ItemDto itemDto;
    private BookingMapper bookingMapper;

    @BeforeEach
    public void setUp() {
        itemDto = new ItemDto(null, "Дрель", "Простая дрель", true, null);
        userDto = new UserDto("user", "user@user.com");
        UserDto userDto2 = new UserDto("user2", "user2@user.com");
        userService.create(userDto2);
        userService.create(userDto);
        itemService.create(itemDto, 1L);

        User user = userService.getUserIfExists(1L);
        Item item = itemService.getItemIfExists(1L);
        bookingDto = new BookingDto(null, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
                BookingStatus.WAITING, 1L, user, item);
    }

    @Test
    void shouldCreateTest() {
        BookingDto createdBookingDto = bookingService.create(bookingDto, 2L);
        assertNotNull(createdBookingDto.getId());

        assertEquals(bookingDto.getStart(), createdBookingDto.getStart());
        assertEquals(bookingDto.getEnd(), createdBookingDto.getEnd());
        assertEquals(bookingDto.getStatus(), createdBookingDto.getStatus());
        assertEquals(bookingDto.getItemId(), createdBookingDto.getItemId());
        assertEquals(bookingDto.getBooker(), createdBookingDto.getBooker());
        assertEquals(bookingDto.getItem(), createdBookingDto.getItem());
    }

    @Test
    void shouldUpdateTest() {
        BookingDto createdBookingDto = bookingService.create(bookingDto, 2L);
        assertNotNull(createdBookingDto.getId());

        BookingDto actualBookingDto = bookingService.update(1L, 1L, true);
        assertEquals(BookingStatus.APPROVED, actualBookingDto.getStatus());
    }

    @Test
    void getByIdTest() {
        BookingDto createdBookingDto = bookingService.create(bookingDto, 2L);
        assertNotNull(createdBookingDto.getId());

        BookingDto actualBooking = bookingService.getById(1L, 2L);

        assertEquals(bookingDto.getItem(), actualBooking.getItem());
        assertEquals(bookingDto.getBooker(), actualBooking.getBooker());
        assertEquals(bookingDto.getStatus(), actualBooking.getStatus());
        assertEquals(bookingDto.getItemId(), actualBooking.getItemId());
    }

    @Test
    void getBookingsForUserTest() {
        BookingDto createdBookingDto = bookingService.create(bookingDto, 2L);
        assertNotNull(createdBookingDto.getId());

        List<BookingDto> bookingDtoList = bookingService.getBookingsForUser("ALL", 2L,0,1);
        assertNotNull(bookingDtoList);
        assertEquals(1, bookingDtoList.size());

        List<BookingDto> bookingDtoList2 = bookingService
                .getBookingsForUser("FUTURE", 2L,0,1);
        assertEquals(bookingDtoList ,bookingDtoList2);

        List<BookingDto> bookingDtoList3 = bookingService
                .getBookingsForUser("REJECTED", 2L,0,1);
        assertEquals(new ArrayList<>() ,bookingDtoList3);
        List<BookingDto> bookingDtoList4 = bookingService
                .getBookingsForUser("CURRENT", 2L,0,1);
        assertEquals(new ArrayList<>() ,bookingDtoList4);
        List<BookingDto> bookingDtoList5 = bookingService
                .getBookingsForUser("PAST", 2L,0,1);
        assertEquals(new ArrayList<>() ,bookingDtoList5);
        List<BookingDto> bookingDtoList6 = bookingService
                .getBookingsForUser("WAITING", 2L,0,1);
        assertEquals(bookingDtoList ,bookingDtoList6);
    }

    @Test
    void getBookingsForOwnerTest() {
        BookingDto createdBookingDto = bookingService.create(bookingDto, 2L);
        assertNotNull(createdBookingDto.getId());

        List<BookingDto> bookingDtoList = bookingService
                .getBookingsForOwner("ALL", 1L,0,1);
        assertNotNull(bookingDtoList);
        assertEquals(1, bookingDtoList.size());

        List<BookingDto> bookingDtoList2 = bookingService
                .getBookingsForOwner("FUTURE", 1L,0,1);
        assertEquals(bookingDtoList ,bookingDtoList2);

        List<BookingDto> bookingDtoList3 = bookingService
                .getBookingsForOwner("REJECTED", 1L,0,1);
        assertEquals(new ArrayList<>() ,bookingDtoList3);
        List<BookingDto> bookingDtoList4 = bookingService
                .getBookingsForOwner("CURRENT", 1L,0,1);
        assertEquals(new ArrayList<>() ,bookingDtoList4);
        List<BookingDto> bookingDtoList5 = bookingService
                .getBookingsForOwner("PAST", 1L,0,1);
        assertEquals(new ArrayList<>() ,bookingDtoList5);
        List<BookingDto> bookingDtoList6 = bookingService
                .getBookingsForOwner("WAITING", 1L,0,1);
        assertEquals(bookingDtoList,bookingDtoList6);

    }

    @Test
    void getBookingIfExists() {
        BookingDto createdBookingDto = bookingService.create(bookingDto, 2L);
        assertNotNull(createdBookingDto.getId());

        Booking booking = bookingService.getBookingIfExists(1L);
        assertNotNull(booking);

        assertEquals(bookingDto.getStatus(), booking.getStatus());
        assertEquals(bookingDto.getBooker(), booking.getBooker());
        assertEquals(bookingDto.getItem(), booking.getItem());

        bookingMapper = new BookingMapper();
        bookingMapper.toBookingDtoForItem(booking);
    }
}