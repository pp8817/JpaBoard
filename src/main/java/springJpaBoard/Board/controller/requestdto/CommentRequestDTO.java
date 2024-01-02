package springJpaBoard.Board.controller.requestdto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CommentRequestDTO {

    private Long bno;

    @NotBlank(message = "공백은 불가능합니다.")
    private String content;

    public CommentRequestDTO() {
    }
}
