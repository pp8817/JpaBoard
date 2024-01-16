package springJpaBoard.Board.board;

import org.jetbrains.annotations.NotNull;

import static springJpaBoard.Board.controller.boarddto.BoardDto.ModifyBoardRequest;
import static springJpaBoard.Board.controller.boarddto.BoardDto.ModifyBoardResponse;

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
}
