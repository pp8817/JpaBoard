package springJpaBoard.Board.comment;

import springJpaBoard.Board.domain.comment.model.Comment;

import static springJpaBoard.Board.domain.comment.dto.CommentDto.CommentResponse;
import static springJpaBoard.Board.domain.comment.dto.CommentDto.CreateCommentRequest;

public class CommentTemplate {

    public static Comment getComment() {
        return Comment.builder()
                .bno(1L)
                .writer("writer")
                .content("content")
                .build();
    }

    public static CreateCommentRequest getCommentRequest() {
        return CreateCommentRequest.builder()
                .bno(1L)
                .content("content")
                .build();
    }

    public static CommentResponse getCommentResponse() {
        return CommentResponse.builder()
                .bno(1L)
                .writer("writer")
                .content("content")
                .build();
    }
}
