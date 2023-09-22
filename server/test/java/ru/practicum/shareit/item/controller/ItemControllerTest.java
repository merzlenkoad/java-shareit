package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.util.BookingStatus;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ItemService itemService;
    private Comment comment;
    private ItemDto itemDto;
    private ItemDtoWithBooking itemDtoWithBooking;
    private BookingDtoForItem lastBooking;
    private BookingDtoForItem nextBooking;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    public void setUp() throws Exception {
        itemDto = new ItemDto(1L, "Дрель", "Простая дрель", true, null);
        start = LocalDateTime.now().plusMinutes(1);
        end = start.plusDays(1);
        lastBooking = BookingDtoForItem
                .builder()
                .id(1L)
                .bookerId(1L)
                .bookingStatus(BookingStatus.WAITING)
                .start(start)
                .end(end)
                .build();
        nextBooking = BookingDtoForItem
                .builder()
                .id(1L)
                .bookerId(1L)
                .bookingStatus(BookingStatus.WAITING)
                .start(start.plusDays(2))
                .end(end.plusDays(2))
                .build();
        itemDtoWithBooking = new ItemDtoWithBooking(
                1L,"Дрель", "Простая дрель", 1L,
                true,lastBooking,nextBooking,null,null);
    }

    @Test
    public void shouldCreateTest() throws Exception {
        when(itemService.create(any(), anyLong())).thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description").value("Простая дрель"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void shouldUpdateTest() throws Exception {
        itemDto.setAvailable(false);

        when(itemService.update(any(),anyLong(),anyLong())).thenReturn(itemDto);

        mvc.perform(patch("/items/{itemId}", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description").value("Простая дрель"))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    public void shouldGetByIdTest() throws Exception {
        when(itemService.getById(anyLong(), anyLong())).thenReturn(itemDtoWithBooking);

        mvc.perform(get("/items/{itemId}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description").value("Простая дрель"))
                .andExpect(jsonPath("ownerId").value(1))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.lastBooking").isNotEmpty())
                .andExpect(jsonPath("$.nextBooking").isNotEmpty());
    }

    @Test
    public void shouldGetItemsByOwnerTest() throws Exception {
        when(itemService.getItemsByOwner(anyLong())).thenReturn(List.of(itemDtoWithBooking, itemDtoWithBooking));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Дрель"))
                .andExpect(jsonPath("$[0].description").value("Простая дрель"))
                .andExpect(jsonPath("[0]ownerId").value(1))
                .andExpect(jsonPath("$[0].available").value(true))
                .andExpect(jsonPath("$[0].lastBooking").isNotEmpty())
                .andExpect(jsonPath("$[0].nextBooking").isNotEmpty());
    }

    @Test
    public void shouldGetItemsBySearchTest() throws Exception {
        when(itemService.getItemsBySearch(anyString())).thenReturn(List.of(itemDto, itemDto));

        mvc.perform(get("/items/search?text=Дрель"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Дрель"))
                .andExpect(jsonPath("$[0].description").value("Простая дрель"))
                .andExpect(jsonPath("$[0].available").value(true));
    }

    @Test
    public void shouldAddCommentTest() throws Exception {
        Item item = new Item(1L, "Дрель", "Простая дрель", true, 1L, null);
        comment = Comment
                .builder()
                .id(1L)
                .authorName("author")
                .item(item)
                .text("some text")
                .created(start)
                .build();

        when(itemService.addComment(anyLong(),anyLong(),any())).thenReturn(comment);

        mvc.perform(post("/items/{itemId}/comment", 1)
                        .content(objectMapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("some text"))
                .andExpect(jsonPath("$.item").isNotEmpty())
                .andExpect(jsonPath("$.authorName").value("author"));
    }
}