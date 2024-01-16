package springJpaBoard.Board.comment;

import org.jetbrains.annotations.NotNull;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;

import static springJpaBoard.Board.controller.commentdto.CommentDto.CommentResponse;
import static springJpaBoard.Board.controller.commentdto.CommentDto.CreateCommentRequest;

public class CommentTemplate {

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

    public static Member getMember() {
        return Member.builder()
                .id(1L)
                .name("1")
                .gender("남성")
                .loginId("1")
                .password("1")
                .address(new Address("1", "1", "1"))
                .build();
    }

    @NotNull
    public static Board getBoard() {
        return Board.builder()
                .title("title")
                .writer("writer")
                .content("content")
                .build();
    }
}
