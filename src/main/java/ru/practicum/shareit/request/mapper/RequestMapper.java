package ru.practicum.shareit.request.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RequestMapper {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemRequest toItemRequest(ItemRequestShortDto itemRequestShortDto, Long userId) {
        return ItemRequest
                .builder()
                .description(itemRequestShortDto.getDescription())
                .created(LocalDateTime.now())
                .requestorId(userId)
                .build();
    }

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(
                itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated(),
                itemRepository.findByRequestIdOrderByIdDesc(
                        itemRequest.getId()).stream().map(itemMapper::toItemShortDto).collect(Collectors.toList())
        );
    }
}
