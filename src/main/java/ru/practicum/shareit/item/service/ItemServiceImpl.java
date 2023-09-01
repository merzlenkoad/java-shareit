package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private ItemMapper mapper;
    private ItemStorage itemStorage;
    private UserStorage userStorage;

    @Override
    public Item create(ItemDto itemDto, Long ownerId) {
        userStorage.idVerification(ownerId);
        return itemStorage.create(mapper.toItem(itemDto, ownerId));
    }

    @Override
    public Item update(ItemDto itemDto, Long itemId, Long ownerId) {
        userStorage.idVerification(ownerId);
        Item item = mapper.toItem(itemDto,ownerId);
        item.setId(itemId);
        return itemStorage.update(item);
    }

    @Override
    public Item getById(Long itemId) {
        return itemStorage.getById(itemId);
    }

    @Override
    public List<Item> getItemsByOwner(Long ownerId) {
        userStorage.idVerification(ownerId);
        return itemStorage.getItemsByOwner(ownerId);
    }

    @Override
    public List<Item> getItemsBySearch(String text) {
        if (!text.isBlank()) {
            return itemStorage.getItemsBySearch(text);
        } else {
            return new ArrayList<>();
        }
    }
}
