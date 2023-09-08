package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item create(@Valid @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Received a POST request: adding an item.");
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                       @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Received a PATCH request: updating the item with id={}", itemId);
        return itemService.update(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public Item getById(@PathVariable Long itemId) {
        log.info("A GET request was received: getting a thing with id={}", itemId);
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<Item> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("A GET request was received: getting all the owner's items with id={}", ownerId);
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<Item> getItemsBySearch(@RequestParam String text) {
        log.info("A GET request was received: getting a thing by search. Request text - {}", text);
        return itemService.getItemsBySearch(text);
    }
}
