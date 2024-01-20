package springJpaBoard.Board.board;

import org.jetbrains.annotations.NotNull;
import springJpaBoard.Board.domain.board.dto.BoardDto;

import static springJpaBoard.Board.domain.board.dto.BoardDto.ModifyBoardRequest;
import static springJpaBoard.Board.domain.board.dto.BoardDto.ModifyBoardResponse;

public class BoardTemplate {

    @NotNull
    public static ModifyBoardRequest getModifyBoardRequest() {
        return ModifyBoardRequest.builder()
                .title("1")
                .content("1")
                .build();
    }

    @NotNull
    public static ModifyBoardResponse getModifyBoardResponse() {
        return ModifyBoardResponse.builder()
                .id(1L)
                .title("2")
                .writer("writer")
                .content("2")
                .build();
    }

    @NotNull
    public static BoardDto.CreateBoardRequest getCreateBoardRequest() {
        return BoardDto.CreateBoardRequest.builder()
                .title("title")
                .writer("username")
                .content("content")
                .build();
    }
}
