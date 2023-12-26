package springJpaBoard.Board.controller.responsedto;

import lombok.Getter;
import springJpaBoard.Board.domain.Board;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {

    private Long id;

    private String title;

    private String content;

    private String writer;

    private int view;

    private int commentCount;

    private int recommend;

    private LocalDateTime boardDateTime;

    private LocalDateTime modifyDateTime;



    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.view = board.getView();
        this.boardDateTime = board.getBoardDateTime();
        this.modifyDateTime = board.getModifyDateTime();
        this.commentCount = board.getCommentCount();
    }
}
