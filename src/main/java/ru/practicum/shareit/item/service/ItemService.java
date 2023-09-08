package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(ItemDto itemDto, Long ownerId);

    Item update(ItemDto itemDto, Long itemId, Long ownerId);

    Item getById(Long itemId);

    List<Item> getItemsByOwner(Long ownerId);

    List<Item> getItemsBySearch(String text);
}
