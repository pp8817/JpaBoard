package springJpaBoard.Board.controller.responsedto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import springJpaBoard.Board.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponseDto {

    private String writer;

    private String content;

    private LocalDateTime localDateTime;


    public CommentResponseDto(Comment comment) {
        this.writer = comment.getWriter();
        this.content = comment.getContent();
        this.localDateTime = comment.getCreateDateTime();
    }

    public CommentResponseDto() {
    }
}
