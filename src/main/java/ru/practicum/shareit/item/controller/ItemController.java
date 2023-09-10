package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Received a POST request: adding an item.");
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Received a PATCH request: updating the item with id={}", itemId);
        return itemService.update(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBooking getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("A GET request was received: getting a thing with id={}", itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoWithBooking> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("A GET request was received: getting all the owner's items with id={}", ownerId);
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam String text) {
        log.info("A GET request was received: getting a thing by search. Request text - {}", text);
        return itemService.getItemsBySearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public Comment addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @RequestBody CommentDto commentDto,
                              @PathVariable("itemId") Long itemId) {
        log.info("A POST request was received: add comment.");
        return itemService.addComment(userId, itemId, commentDto);
    }
}
