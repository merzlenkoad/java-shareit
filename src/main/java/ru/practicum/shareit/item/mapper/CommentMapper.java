package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Component
public class CommentMapper {
    public Comment toComment(CommentDto commentDto, User author, Item item) {
        return Comment
                .builder()
                .authorName(author.getName())
                .created(LocalDateTime.now())
                .text(commentDto.getText())
                .item(item)
                .build();
    }
}
