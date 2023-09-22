package ru.practicum.shareit.request.dto;

import lombok.*;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestShortDto {

    private String description;
}
