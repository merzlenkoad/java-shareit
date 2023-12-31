package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    BookingDto create(BookingDto bookingDto, Long userId);

    BookingDto update(Long bookingId, Long userId, Boolean approved);

    BookingDto getById(Long bookingId, Long userId);

    List<BookingDto> getBookingsForUser(String state, Long userId, Integer from, Integer size);

    List<BookingDto> getBookingsForOwner(String state, Long ownerId, Integer from, Integer size);

    Booking getBookingIfExists(Long bookingId);
}
