package springJpaBoard.Board;

import org.jetbrains.annotations.NotNull;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;

public class UtilsTemplate {
    @NotNull
    public static Board getBoard() {
        return Board.builder()
                .title("title")
                .writer("writer")
                .content("content")
                .build();
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
