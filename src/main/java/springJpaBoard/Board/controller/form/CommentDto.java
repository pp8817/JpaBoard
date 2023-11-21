package springJpaBoard.Board.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto {
    private Long bno;
    private String writer;
    private String content;

    public void createComment(String content) {
        this.content = content;
    }
}
