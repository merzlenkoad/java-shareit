package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(groups = CreateGroup.class)
    private String name;

    @Email(groups = CreateGroup.class)
    @NotBlank(groups = CreateGroup.class)
    @Email
    private String email;
}
