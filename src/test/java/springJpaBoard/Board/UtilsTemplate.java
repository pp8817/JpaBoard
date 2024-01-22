package springJpaBoard.Board;

import org.jetbrains.annotations.NotNull;
import org.springframework.mock.web.MockHttpSession;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.etc.Address;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.global.constans.SessionConst;

public class UtilsTemplate {
    @NotNull
    public static MockHttpSession getSession(Member member) {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        return session;
    }

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
