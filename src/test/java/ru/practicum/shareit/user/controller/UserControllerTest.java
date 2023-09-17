package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.handler.exception.BookingYourOwnThingException;
import ru.practicum.shareit.handler.exception.NotFoundException;
import ru.practicum.shareit.handler.exception.NotOwnerException;
import ru.practicum.shareit.handler.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");

        when(userService.create(any())).thenReturn(
                new User(1L, request.getName(), request.getEmail()));

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@user.com"))
                .andExpect(jsonPath("$.name").value("user"));
    }

    @Test
    public void shouldUpdateTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");
        User user = new User(1L, "update","update@user.com");

        when(userService.update(any(), anyLong())).thenReturn(user);

        mockMvc.perform(patch("/users/1")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("update@user.com"))
                .andExpect(jsonPath("$.name").value("update"));
    }

    @Test
    public void shouldGetByIdTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");

        when(userService.getById(any())).thenReturn(new User(1L, request.getName(), request.getEmail()));

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user@user.com"))
                .andExpect(jsonPath("$.name").value("user"));
    }

    @Test
    public void shouldGetAllTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");
        User user = new User(1L, request.getName(), request.getEmail());

        when(userService.getAll()).thenReturn(List.of(user,user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("user@user.com"))
                .andExpect(jsonPath("$[0].name").value("user"));
    }

    @Test
    public void shouldDeleteById() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void handlerNotFoundExceptionTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");

        when(userService.create(any())).thenThrow(new NotFoundException("some text",1L));

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void handlerBookingYourOwnThingExceptionTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");

        when(userService.create(any())).thenThrow(new BookingYourOwnThingException("some text"));

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void handlerNotOwnerExceptionTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");

        when(userService.create(any())).thenThrow(new NotOwnerException("some text"));

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void handlerValidationExceptionTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");

        when(userService.create(any())).thenThrow(new ValidationException("some text"));

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void handlerSQLExceptionTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");

        given(userService.create(any())).willAnswer( invocation -> { throw new SQLException(); });

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void handlerRuntimeExceptionTest() throws Exception {
        UserDto request = new UserDto("user", "user@user.com");

        when(userService.create(any())).thenThrow(new RuntimeException());

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}