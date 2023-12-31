package ru.practicum.shareit.request.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RequestMapper {
    private final ItemMapper itemMapper;

    public ItemRequest toItemRequest(ItemRequestShortDto itemRequestShortDto, Long userId) {
        return ItemRequest
                .builder()
                .description(itemRequestShortDto.getDescription())
                .created(LocalDateTime.now())
                .requestorId(userId)
                .build();
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<Item> items) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                items.stream().map(itemMapper::toItemShortDto).collect(Collectors.toList())
        );
    }
}
