package ru.practicum.shareit.item.mapper;

import org.h2.mvstore.DataUtils;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.BookingStatus;
import ru.practicum.shareit.util.DateUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemMapperUnitTest {
    private ItemMapper itemMapper;
    private DateUtils dateUtils;

    @Test
    void toItemDtoWithBooking() {
        itemMapper = new ItemMapper(new BookingMapper());
        dateUtils = new DateUtils();
        User booker = new User(1L,"user","user@user.com");
        Item item = new Item(1L,"Дрель","Простая дрель", true, 1L, 1L);
        Booking booking = new Booking(1L, dateUtils.now().plusDays(1), dateUtils.now().plusDays(2),
                item, booker, BookingStatus.WAITING);
        Booking booking2 = new Booking(2L, dateUtils.now().plusDays(1), dateUtils.now().plusDays(2),
                item, booker, BookingStatus.APPROVED);
        Booking booking3 = new Booking(2L, dateUtils.now().minusDays(2), dateUtils.now().minusDays(1),
                item, booker, BookingStatus.APPROVED);
        Comment comment = new Comment(1L,"some text", item, "user", dateUtils.now());
        List<Booking> bookings = List.of(booking,booking2,booking3);
        List<Comment> comments = List.of(comment);

        ItemDtoWithBooking itemDtoWithBooking =
                itemMapper.toItemDtoWithBooking(item, bookings, booker.getId(), comments);
        assertNotNull(itemDtoWithBooking);
    }
}