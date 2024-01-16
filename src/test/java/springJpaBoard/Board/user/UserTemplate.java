package springJpaBoard.Board.user;

import org.jetbrains.annotations.NotNull;
import springJpaBoard.Board.domain.Address;

import static springJpaBoard.Board.controller.memberdto.MemberDto.MemberResponse;

public class UserTemplate {

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
