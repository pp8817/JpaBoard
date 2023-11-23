package springJpaBoard.Board.controller.responsedto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponseDto {
    private Long id;

    private Long bno;

    private String writer;

    private String content;

    private Member member;

    private LocalDateTime localDateTime;


    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.bno = comment.getBno();
        this.writer = comment.getMember().getName();
        this.content = comment.getContent();
        this.localDateTime = comment.getCreateDateTime();
    }

    public CommentResponseDto() {
    }
}
