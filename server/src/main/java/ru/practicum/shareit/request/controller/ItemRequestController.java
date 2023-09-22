package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                 @RequestBody ItemRequestShortDto itemRequestShortDto) {
        log.info("Received a POST request: adding a thing request.");
        return requestService.create(itemRequestShortDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getByUserId(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Received a GET request: getting a list of your own queries.");
        return requestService.getByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequests(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                               @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Received a GET request: getting a list of all requests.");
        return requestService.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                         @PathVariable(name = "requestId") Long requestId) {
        log.info("Received a GET request: getting a specific request by id.");
        return requestService.getRequestById(userId, requestId);
    }

}
