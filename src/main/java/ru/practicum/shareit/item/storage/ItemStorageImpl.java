package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.handler.exception.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemStorageImpl implements ItemStorage {
    private Map<Long, Item> items;
    private Long id;

    public ItemStorageImpl() {
        this.items = new HashMap<>();
        this.id = 0L;
    }

    @Override
    public Item create(Item item) {
        item.setId(++id);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        idVerification(item.getId());
        if (items.get(item.getId()).getOwnerId().equals(item.getOwnerId())) {
            if (item.getName() != null && item.getDescription() != null) {
                items.replace(item.getId(), item);
            } else if (item.getName() != null) {
                items.get(item.getId()).setName(item.getName());
            } else if (item.getDescription() != null) {
                items.get(item.getId()).setDescription(item.getDescription());
            } else {
                items.get(item.getId()).setAvailable(item.isAvailable());
            }
            return items.get(item.getId());
        } else {
            throw new NotFoundException("Не верно указан владелец. id=", item.getOwnerId());
        }

    }

    @Override
    public Item getById(Long itemId) {
        idVerification(itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getItemsByOwner(Long ownerId) {
        return items.values().stream()
                .filter(i -> i.getOwnerId().equals(ownerId)).collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemsBySearch(String text) {
        String actualText = text.toLowerCase();
        return items.values().stream().filter(i -> i.isAvailable()
                && (i.getName().toLowerCase().contains(actualText)
                || i.getDescription().toLowerCase().contains(actualText)))
                .collect(Collectors.toList());
    }

    @Override
    public void idVerification(Long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Предмет не найден.", itemId);
        }
    }
}
