package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    Item update(Item item);

    Item getById(Long itemId);

    List<Item> getItemsByOwner(Long ownerId);

    List<Item> getItemsBySearch(String text);

    void idVerification(Long itemId);
}
