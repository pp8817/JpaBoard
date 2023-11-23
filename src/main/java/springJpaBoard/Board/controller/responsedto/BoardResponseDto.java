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

    private LocalDateTime boardDateTime;

    private LocalDateTime modifyDateTime;

//    private List<CommentResponseDto> Comments;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.writer = board.getWriter();
        this.view = board.getView();
        this.boardDateTime = board.getBoardDateTime();
        this.modifyDateTime = board.getModifyDateTime();
//        this.Comments = board.getCommentList().stream()
//                .map(comment -> new CommentResponseDto(comment))
//                .collect(toList());
    }
}
