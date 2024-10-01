package ru.practicum.part.private_part.comment.service;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentOutDto;

public interface PrivateCommentService {
    CommentOutDto addComment(Integer userId, Integer eventId, CommentDto commentDto) throws Exception;

    CommentOutDto editComment(Integer userId, Integer eventId, Integer commId, CommentDto commentDto) throws Exception;

    void deleteComment(Integer userId, Integer eventId, Integer commId) throws Exception;
}
