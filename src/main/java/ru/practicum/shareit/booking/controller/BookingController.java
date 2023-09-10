package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestBody BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Received a POST request: adding a booking.");
        return bookingService.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam Boolean approved) {
        log.info("Received a PATCH request: changing the booking status.");
        return bookingService.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@PathVariable Long bookingId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Received a GET request: getting a booking with id = {}", bookingId);
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookingsForUser(@RequestParam(required = false, defaultValue = "ALL") String state,
                                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Received a GET request: getting a bookings, userId = {}", userId);
        return bookingService.getBookingsForUser(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsForOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
                                                @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Received a GET request: getting a bookings, ownerId = {}", ownerId);
        return bookingService.getBookingsForOwner(state, ownerId);
    }
}
