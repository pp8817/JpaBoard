package springJpaBoard.Board.board;

import org.jetbrains.annotations.NotNull;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;

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


    @NotNull
    public static Board getBoard() {
        Board board = Board.builder()
                .title("title")
                .writer("writer")
                .content("content")
                .build();
        return board;
    }


    @NotNull
    public static Member getMember() {
        return Member.builder()
                .id(1L)
                .name("1")
                .gender("남성")
                .loginId("1")
                .password("1")
                .address(new Address("1", "1", "1"))
                .build();
    }
}
