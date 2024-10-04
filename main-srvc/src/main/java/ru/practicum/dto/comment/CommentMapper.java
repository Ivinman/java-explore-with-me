package ru.practicum.dto.comment;

import ru.practicum.model.comment.Comment;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setComment(commentDto.getText());
        return comment;
    }

    public static CommentOutDto toCommOutDto(Comment comment) {
        CommentOutDto commentOutDto = new CommentOutDto();
        commentOutDto.setEventTitle(comment.getEvent().getTitle());
        commentOutDto.setUserName(comment.getUser().getName());
        commentOutDto.setText(comment.getComment());
        commentOutDto.setId(comment.getId());
        return commentOutDto;
    }

    public static CommentOutForEventDto toCommOutForEvent(Comment comment) {
        CommentOutForEventDto commentOut = new CommentOutForEventDto();
        commentOut.setUserName(comment.getUser().getName());
        commentOut.setText(comment.getComment());
        return commentOut;
    }
}
