package ru.practicum.shareit.item.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.util.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ItemMapper {
    private BookingMapper bookingMapper;

    public Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                ownerId
        );
    }

    public ItemDto toItemDto(Item item) {
       return new ItemDto(
               item.getId(),
               item.getName(),
               item.getDescription(),
               item.getAvailable()
       );
    }

    public ItemDtoWithBooking toItemDtoWithBooking(
            Item item, List<Booking> bookings, Long userId, List<Comment> comments) {
        LocalDateTime time = LocalDateTime.now();

        Optional<Booking> lastBooking = bookings.stream()
                .filter(b -> b.getItem().getOwnerId().equals(userId))
                .filter(b -> b.getItem().getId().equals(item.getId()) && b.getStatus().equals(BookingStatus.APPROVED))
                .filter(b -> (b.getStart().isBefore(time) && b.getEnd().isAfter(time)) || b.getEnd().isBefore(time))
                .max(Comparator.comparing(Booking::getId));
        Optional<Booking> nextBooking = bookings.stream()
                .filter(b -> b.getItem().getOwnerId().equals(userId))
                .filter(b -> b.getItem().getId().equals(item.getId()) && b.getStatus().equals(BookingStatus.APPROVED))
                .filter(b -> b.getStart().isAfter(time))
                .min(Comparator.comparing(Booking::getStart));

        BookingDtoForItem actualLastBooking = lastBooking
                .map(booking -> bookingMapper.toBookingDtoForItem(booking)).orElse(null);
        BookingDtoForItem actualNextBooking = nextBooking
                .map(booking -> bookingMapper.toBookingDtoForItem(booking)).orElse(null);

        return ItemDtoWithBooking
                .builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .ownerId(item.getOwnerId())
                .available(item.getAvailable())
                .lastBooking(actualLastBooking)
                .nextBooking(actualNextBooking)
                .comments(comments)
                .build();
    }
}
