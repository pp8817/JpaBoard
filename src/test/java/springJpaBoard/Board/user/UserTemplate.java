package springJpaBoard.Board.user;

import org.jetbrains.annotations.NotNull;
import springJpaBoard.Board.domain.etc.Address;

import static springJpaBoard.Board.domain.member.dto.MemberDto.*;

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

    @NotNull
    public static CreateMemberRequest getCreateMemberRequest() {
        return CreateMemberRequest.builder()
                .loginId("1")
                .password("1")
                .name("1")
                .gender("남성")
                .city("1")
                .street("1")
                .zipcode("1")
                .build();
    }

    @NotNull
    public static ModifyMemberRequest getModifyMemberRequest() {
        return ModifyMemberRequest.builder()
                .id(1L)
                .name("2")
                .gender("여성")
                .address(new Address("2", "2", "2"))
                .build();
    }
}
