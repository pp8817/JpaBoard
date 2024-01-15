package springJpaBoard.Board.user;

import org.jetbrains.annotations.NotNull;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;

import static springJpaBoard.Board.controller.memberdto.MemberDto.MemberResponse;

public class UserTemplate {
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

    @NotNull
    public static MemberResponse updateMember() {

        return MemberResponse.builder()
                .id(1L)
                .name("2")
                .gender("여성")
                .address(new Address("2", "2", "2"))
                .build();
    }
}
