package springJpaBoard.Board.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class UpdateBoardDto {

    private Long id;
    private String title;
    private String content;

    public UpdateBoardDto(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public UpdateBoardDto() {
    }
}
