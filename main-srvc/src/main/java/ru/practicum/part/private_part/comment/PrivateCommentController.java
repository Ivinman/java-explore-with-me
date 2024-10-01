package ru.practicum.part.private_part.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentOutDto;
import ru.practicum.part.private_part.comment.service.PrivateCommentService;

@RestController
@RequestMapping("/user/{userId}/comments")
@RequiredArgsConstructor
public class PrivateCommentController {
    private final PrivateCommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/{eventId}")
    public CommentOutDto addComment(@PathVariable Integer userId, @PathVariable Integer eventId,
                                    @RequestBody CommentDto commentDto) throws Exception {
        return commentService.addComment(userId, eventId, commentDto);
    }

    @PatchMapping("/{commId}/events/{eventId}")
    public CommentOutDto editComment(@PathVariable Integer userId, @PathVariable Integer eventId,
                                     @PathVariable Integer commId,
                                     @RequestBody CommentDto commentDto) throws Exception {
        return commentService.editComment(userId, eventId, commId, commentDto);
    }

    @DeleteMapping("/{commId}/events/{eventId}")
    public void deleteComment(@PathVariable Integer userId, @PathVariable Integer eventId,
                              @PathVariable Integer commId) throws Exception {
        commentService.deleteComment(userId, eventId, commId);
    }
}
