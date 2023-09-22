package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.handler.exception.BookingYourOwnThingException;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.handler.exception.NotOwnerException;
import ru.practicum.shareit.handler.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.BookingStatus;
import ru.practicum.shareit.util.DateUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplUnitTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    @Mock
    private BookingMapper bookingMapper;
    private final DateUtils dateUtils = new DateUtils();

    @Test
    public void createWithOwnerIdEqualsUserId() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", true, 1L, null);
        BookingDto bookingDto = new BookingDto(null,dateUtils.now().plusDays(1),
                dateUtils.now().plusDays(2), BookingStatus.WAITING,1L, user, item);
        when(itemService.getItemIfExists(anyLong())).thenReturn(item);
        when(userService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(BookingYourOwnThingException.class, () -> {
            bookingService.create(bookingDto, 1L);
        });
    }

    @Test
    public void createWithStartIsEmpty() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", true, 2L, null);
        BookingDto bookingDto = new BookingDto(null,null,
                dateUtils.now().plusDays(2), BookingStatus.WAITING,1L, user, item);
        when(itemService.getItemIfExists(anyLong())).thenReturn(item);
        when(userService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(ValidationException.class, () -> {
            bookingService.create(bookingDto, 1L);
        });
    }

    @Test
    public void createWithEndIsEmpty() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", true, 2L, null);
        BookingDto bookingDto = new BookingDto(null,dateUtils.now().plusDays(1),
                null, BookingStatus.WAITING,1L, user, item);
        when(itemService.getItemIfExists(anyLong())).thenReturn(item);
        when(userService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(ValidationException.class, () -> {
            bookingService.create(bookingDto, 1L);
        });
    }

    @Test
    public void createWithStartIsAfterEnd() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", true, 2L, null);
        BookingDto bookingDto = new BookingDto(null,dateUtils.now().plusDays(2),
                dateUtils.now().plusDays(1), BookingStatus.WAITING,1L, user, item);
        when(itemService.getItemIfExists(anyLong())).thenReturn(item);
        when(userService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(ValidationException.class, () -> {
            bookingService.create(bookingDto, 1L);
        });
    }

    @Test
    public void createWithStartIsBeforeNow() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", true, 2L, null);
        BookingDto bookingDto = new BookingDto(null,dateUtils.now().minusDays(1),
                dateUtils.now().plusDays(1), BookingStatus.WAITING,1L, user, item);
        when(itemService.getItemIfExists(anyLong())).thenReturn(item);
        when(userService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(ValidationException.class, () -> {
            bookingService.create(bookingDto, 1L);
        });
    }

    @Test
    public void createWithStartEqualsEnd() {
        LocalDateTime test = dateUtils.now();
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", false, 2L, null);
        BookingDto bookingDto = new BookingDto(null, test,
                test, BookingStatus.WAITING,1L, user, item);
        when(itemService.getItemIfExists(anyLong())).thenReturn(item);
        when(userService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(ValidationException.class, () -> {
            bookingService.create(bookingDto, 1L);
        });
    }

    @Test
    public void updateWithOwnerIdNotEqualsUserId() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", false, 2L, null);
        Booking booking = new Booking(1L, dateUtils.now().plusDays(1),
                dateUtils.now().plusDays(2), item, user, BookingStatus.WAITING);

        when(userService.getUserIfExists(anyLong())).thenReturn(user);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotOwnerException.class, () -> {
            bookingService.update(1L, 1L, true);
        });
    }

    @Test
    public void updateWithStatusApproved() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", false, 2L, null);
        Booking booking = new Booking(1L, dateUtils.now().plusDays(1),
                dateUtils.now().plusDays(2), item, user, BookingStatus.APPROVED);

        when(userService.getUserIfExists(anyLong())).thenReturn(user);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(ValidationException.class, () -> {
            bookingService.update(1L, 2L, true);
        });
    }

    @Test
    public void updateWithApprovedFalse() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", false, 2L, null);
        Booking booking = new Booking(1L, dateUtils.now().plusDays(1),
                dateUtils.now().plusDays(2), item, user, BookingStatus.WAITING);

        when(userService.getUserIfExists(anyLong())).thenReturn(user);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(null);

        bookingService.update(1L, 2L, false);
    }

    @Test
    public void getByIdWithOwnerIdEqualsUserId() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", false, 2L, null);
        Booking booking = new Booking(1L, dateUtils.now().plusDays(1),
                dateUtils.now().plusDays(2), item, user, BookingStatus.WAITING);

        when(userService.getUserIfExists(anyLong())).thenReturn(user);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(null);

        bookingService.getById(1L, 2L);
    }

    @Test
    public void getByIdWithOwnerIdNotEqualsUserId() {
        User user = new User(1L, "user", "user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", false, 2L, null);
        Booking booking = new Booking(1L, dateUtils.now().plusDays(1),
                dateUtils.now().plusDays(2), item, user, BookingStatus.WAITING);

        when(userService.getUserIfExists(anyLong())).thenReturn(user);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotOwnerException.class, () -> {
            bookingService.getById(1L, 3L);
        });
    }

    @Test
    public void getBookingsForUserWithStateUnknown() {
        User user = new User(1L, "user", "user@user.com");

        when(userService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(ValidationException.class, () -> {
            bookingService.getBookingsForUser("Unknown", 1L, 2, 1);
        });
    }

    @Test
    public void getBookingsForOwnerWithStateUnknown() {
        User user = new User(1L, "user", "user@user.com");

        when(userService.getUserIfExists(anyLong())).thenReturn(user);

        assertThrows(ValidationException.class, () -> {
            bookingService.getBookingsForOwner("Unknown", 1L, 2, 1);
        });
    }

    @Test
    public void getBookingIfExists() {
        assertThrows(NotFoundException.class, () -> {
            bookingService.getBookingIfExists(1L);
        });
    }
}