package springJpaBoard.Board.controller.responsedto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import springJpaBoard.Board.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponseDTO {
    private Long id;

    private Long bno;

    private String writer;

    private String content;

    private LocalDateTime localDateTime;


    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.bno = comment.getBno();
        this.writer = comment.getWriter();
        this.content = comment.getContent();
        this.localDateTime = comment.getCreateDateTime();
    }

    public CommentResponseDTO() {
    }
}
