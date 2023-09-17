package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RequestService requestService;

    private ItemRequestDto itemRequestDto;
    private ItemShortDto itemShortDto;
    private ItemRequestShortDto itemRequestShortDto;

    @BeforeEach
    public void setUp() throws Exception {
        itemShortDto = ItemShortDto
                .builder()
                .id(1L)
                .name("Дрель")
                .description("Простая дрель")
                .available(true)
                .requestId(1L)
                .build();
        itemRequestDto = ItemRequestDto
                .builder()
                .id(1L)
                .description("Простая дрель")
                .created(LocalDateTime.now())
                .items(List.of(itemShortDto, itemShortDto))
                .build();
        itemRequestShortDto = new ItemRequestShortDto("Простая дрель");
    }

    @Test
    public void shouldCreateTest() throws Exception {
        when(requestService.create(any(), anyLong())).thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(itemRequestShortDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Простая дрель"))
                .andExpect(jsonPath("$.items").isNotEmpty());
    }

    @Test
    public void shouldGetByUserIdTest() throws Exception {
        when(requestService.getByUserId(anyLong())).thenReturn(List.of(itemRequestDto, itemRequestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Простая дрель"))
                .andExpect(jsonPath("$[0].items").isNotEmpty());
    }

    @Test
    public void shouldGetAllRequestsTest() throws  Exception {
        when(requestService.getAllRequests(anyLong(), anyInt(),anyInt()))
                .thenReturn(List.of(itemRequestDto,itemRequestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Простая дрель"))
                .andExpect(jsonPath("$[0].items").isNotEmpty());
    }

    @Test
    public void shouldGetRequestByIdTest() throws Exception {
        when(requestService.getRequestById(anyLong(), anyLong())).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/{requestId}", 1)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Простая дрель"))
                .andExpect(jsonPath("$.items").isNotEmpty());
    }
}