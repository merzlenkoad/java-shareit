package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto itemDto, Long ownerId);

    ItemDto update(ItemDto itemDto, Long itemId, Long userId);

    ItemDtoWithBooking getById(Long itemId, Long userId);

    List<ItemDtoWithBooking> getItemsByOwner(Long ownerId);

    List<ItemDto> getItemsBySearch(String text);

    Comment addComment(Long userId, Long itemId, CommentDto commentDto);

    Item getItemIfExists(Long itemId);
}
