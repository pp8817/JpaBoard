package springJpaBoard.Board.controller.responsedto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import springJpaBoard.Board.domain.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentResponseDto {

    @NotBlank(message = "작성자는 필수입니다.")
    @Size(min = 1, max = 10, message = "작성자는 1~10 글자 이내만 가능합니다.")
    private String writer;

    @NotBlank(message = "공백은 불가능합니다.")
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