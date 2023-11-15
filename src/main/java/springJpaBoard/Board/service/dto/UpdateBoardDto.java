package springJpaBoard.Board.service.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateBoardDto {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime modifyDateTime;

    public UpdateBoardDto(Long id, String title, String content, LocalDateTime modifyDateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.modifyDateTime = modifyDateTime;
    }

    public UpdateBoardDto() {
    }
}