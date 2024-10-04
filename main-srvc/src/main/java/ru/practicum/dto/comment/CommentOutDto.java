package ru.practicum.dto.comment;

import lombok.Data;

@Data
public class CommentOutDto {
    private Integer id;
    private String eventTitle;
    private String userName;
    private String text;
}
