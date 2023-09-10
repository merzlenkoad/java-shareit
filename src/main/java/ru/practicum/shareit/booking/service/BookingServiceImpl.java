package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.handler.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final BookingMapper mapper;

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto, Long userId) {
        Item item = itemService.checkItemIsExists(bookingDto.getItemId());
        User booker = userService.checkUserIsExists(userId);
        if (item.getOwnerId().equals(userId)) {
            throw new NotFoundException("It is impossible to book your own thing.", userId);
        }
        if (Optional.ofNullable(bookingDto.getStart()).isEmpty() ||
                Optional.ofNullable(bookingDto.getEnd()).isEmpty() ||
                bookingDto.getStart().isAfter(bookingDto.getEnd()) ||
                bookingDto.getStart().isBefore(LocalDateTime.now()) ||
                bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("Incorrect time range of booking.");
        }
        bookingDto.setStatus(BookingStatus.WAITING);
        bookingDto.setItem(item);
        bookingDto.setBooker(booker);
        Booking booking = mapper.toBooking(bookingDto, userId);
        if (!booking.getItem().getAvailable()) {
            throw new ValidationException("Booking of the item is not available");
        }
        return mapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto update(Long bookingId, Long userId, Boolean approved) {
        userService.checkUserIsExists(userId);
        Booking booking = checkBookingIsExists(bookingId);
        if (!booking.getItem().getOwnerId().equals(userId)) {
            throw new NotFoundException("Only the owner can confirm the booking.", userId);
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("The approved status has already been set.");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return mapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(Long bookingId, Long userId) {
        userService.checkUserIsExists(userId);
        Booking booking = checkBookingIsExists(bookingId);
        if (booking.getBooker().getId().equals(userId) ||
                booking.getItem().getOwnerId().equals(userId)) {
            return mapper.toBookingDto(booking);
        } else {
            throw new NotFoundException("The requester is not the owner of the item or the booking",userId);
        }
    }

    @Override
    public List<BookingDto> getBookingsForUser(String state, Long userId) {
        userService.checkUserIsExists(userId);
        LocalDateTime time = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
                break;
            case "CURRENT":
                bookings = bookingRepository
                        .findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByStartDesc(
                                userId, time, time);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, time);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, time);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(mapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsForOwner(String state, Long ownerId) {
        userService.checkUserIsExists(ownerId);
        LocalDateTime time = LocalDateTime.now();
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(ownerId);
                break;
            case "FUTURE":
                bookings = bookingRepository
                        .findByItemOwnerIdAndStartIsAfterOrderByStartDesc(ownerId, time);
                break;
            case "WAITING":
                bookings = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
                break;
            case "REJECTED":
                bookings = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
                break;
            case "CURRENT":
                bookings = bookingRepository
                        .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, time, time);
                break;
            case "PAST":
                bookings = bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, time);
                break;
            default:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream().map(mapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public Booking checkBookingIsExists(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found. Id = ", bookingId));
    }
}
