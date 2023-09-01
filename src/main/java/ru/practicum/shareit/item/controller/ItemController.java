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
    private ItemService itemService;

    @PostMapping
    public Item create(@Valid @RequestBody ItemDto itemDto,
                       @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен POST-запрос: добавление предмета.");
        return itemService.create(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                       @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен PATCH-запрос: обновление вещи с id={}", itemId);
        return itemService.update(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public Item getById(@PathVariable Long itemId) {
        log.info("Получен GET-запрос: получение вещи с id={}", itemId);
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<Item> getItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен GET-запрос: получение всех вещей владельца с id={}", ownerId);
        return itemService.getItemsByOwner(ownerId);
    }

    @GetMapping("/search")
    public List<Item> getItemsBySearch(@RequestParam String text) {
        log.info("Получен GET-запрос: получение вещи по поиску. Текст запроса - {}", text);
        return itemService.getItemsBySearch(text);
    }
}
