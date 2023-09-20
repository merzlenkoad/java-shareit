package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestShortDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class RequestServiceImplIntegrationTest {

    @Autowired
    private RequestService requestService;
    @Autowired
    private UserService userService;
    private ItemRequestDto itemRequestDto;
    private ItemRequestShortDto itemRequestShortDto;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = new UserDto("user", "user@user.com");
        userService.create(userDto);
        itemRequestShortDto = new ItemRequestShortDto("Дрель");
    }

    @Test
    void shouldCreateTest() {
        itemRequestDto =  requestService.create(itemRequestShortDto, 1L);
        assertNotNull(itemRequestDto.getId());

        assertEquals(itemRequestShortDto.getDescription(), itemRequestDto.getDescription());
    }

    @Test
    void getByUserId() {
        itemRequestDto =  requestService.create(itemRequestShortDto, 1L);
        assertNotNull(itemRequestDto.getId());

        List<ItemRequestDto> itemRequestDtoList = requestService.getByUserId(1L);
        assertNotNull(itemRequestDtoList);
        assertEquals(1, itemRequestDtoList.size());
    }

    @Test
    void getAllRequests() {
        itemRequestDto =  requestService.create(itemRequestShortDto, 1L);
        assertNotNull(itemRequestDto.getId());

        UserDto userDto2 = new UserDto("user2", "user2@user.com");
        userService.create(userDto2);

        ItemRequestShortDto itemRequestShortDto2 = new ItemRequestShortDto("Дрель2");
        requestService.create(itemRequestShortDto2, 1L);

        List<ItemRequestDto> itemRequestDtoList = requestService.getAllRequests(2L, 0, 10);
        requestService.getAllRequests(2L, 1, 10);
        assertNotNull(itemRequestDtoList);
        assertEquals(2, itemRequestDtoList.size());
    }

    @Test
    void getRequestById() {
        itemRequestDto =  requestService.create(itemRequestShortDto, 1L);
        assertNotNull(itemRequestDto.getId());

        ItemRequestDto actualItemRequestDto = requestService.getRequestById(1L, 1L);
        assertNotNull(itemRequestDto);
        assertEquals(itemRequestDto.getId(), actualItemRequestDto.getId());
        assertEquals(itemRequestDto.getDescription(), actualItemRequestDto.getDescription());
        assertEquals(itemRequestDto.getItems(), actualItemRequestDto.getItems());
    }
}