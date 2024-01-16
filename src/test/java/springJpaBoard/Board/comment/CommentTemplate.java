package springJpaBoard.Board.comment;

import springJpaBoard.Board.domain.Comment;

import static springJpaBoard.Board.controller.commentdto.CommentDto.CommentResponse;
import static springJpaBoard.Board.controller.commentdto.CommentDto.CreateCommentRequest;

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
