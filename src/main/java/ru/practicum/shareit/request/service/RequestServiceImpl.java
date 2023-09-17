package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final UserService userService;

    @Override
    public ItemRequestDto create(ItemRequestShortDto itemRequestShortDto, Long userId) {
        userService.getUserIfExists(userId);
        ItemRequest itemRequest = requestMapper.toItemRequest(itemRequestShortDto, userId);
        return requestMapper.toItemRequestDto(requestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getByUserId(Long userId) {
        userService.getUserIfExists(userId);
        List<ItemRequest> itemRequests = requestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return  itemRequests.stream().map(requestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        userService.getUserIfExists(userId);
        List<ItemRequest> itemRequests = requestRepository
                .findByRequestorIdIsNotOrderByCreatedDesc(userId, PageRequest.of(from > 0 ? from / size : 0, size));
        return itemRequests.stream().map(requestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        userService.getUserIfExists(userId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found.", requestId));
        return requestMapper.toItemRequestDto(itemRequest);
    }
}
