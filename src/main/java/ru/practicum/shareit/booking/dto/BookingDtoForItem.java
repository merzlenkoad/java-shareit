package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.util.BookingStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoForItem {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long bookerId;
    private BookingStatus bookingStatus;
}
