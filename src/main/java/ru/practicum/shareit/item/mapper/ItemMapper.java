package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription(),
                Boolean.parseBoolean(itemDto.getAvailable()),
                ownerId
        );
    }
}
