package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.handler.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemMapper mapper;
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;


    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        userService.checkUserIsExists(ownerId);
        return mapper.toItemDto(itemRepository.save(mapper.toItem(itemDto, ownerId)));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long itemId, Long userId) {
        userService.checkUserIsExists(userId);
        Item item = checkItemIsExists(itemId);
        if (item.getOwnerId().equals(userId)) {
            if (!Strings.isBlank(itemDto.getName())) {
                item.setName(itemDto.getName());
            }
            if (!Strings.isBlank(itemDto.getDescription())) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            return mapper.toItemDto(itemRepository.save(item));
        } else  {
            throw new NotFoundException("It is impossible to edit someone else's thing. id = ", itemId);
        }
    }

    @Override
    public ItemDtoWithBooking getById(Long itemId, Long userId) {
        User user = userService.checkUserIsExists(userId);
        Item item = checkItemIsExists(itemId);
        List<Booking> bookings = bookingRepository.findByItemId(itemId);
        List<Comment> comments = commentRepository.findByItemId(itemId);

        return mapper.toItemDtoWithBooking(item, bookings, userId, comments);
    }

    @Override
    public List<ItemDtoWithBooking> getItemsByOwner(Long ownerId) {
        User user = userService.checkUserIsExists(ownerId);
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<Booking> bookings = bookingRepository.findByItemOwnerId(ownerId);
        List<Comment> comments = commentRepository.findByAuthorName(user.getName());

        List<ItemDtoWithBooking> itemsDtoWithBooking = new ArrayList<>();
        for (Item item: items) {
            itemsDtoWithBooking.add(mapper.toItemDtoWithBooking(item, bookings, ownerId, comments));
        }
        return itemsDtoWithBooking;
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        if (!text.isBlank()) {
            List<Item> items = itemRepository.search(text);
            List<ItemDto> itemsDto = new ArrayList<>();
            for (Item item: items) {
                itemsDto.add(mapper.toItemDto(item));
            }
            return itemsDto;
        } else {
           return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public Comment addComment(Long userId, Long itemId, CommentDto commentDto) {
        User user = userService.checkUserIsExists(userId);
        Item item = checkItemIsExists(itemId);
        List<Booking> bookings = bookingRepository
                .findByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new ValidationException("The user did not book the item or the booking period has not expired");
        } else {
            return commentRepository.save(commentMapper.toComment(commentDto, user, item));
        }
    }

    @Override
    public Item checkItemIsExists(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found. id = ", itemId));
    }
}
