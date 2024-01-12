package springJpaBoard.Board.controller.memberdto;

import lombok.Builder;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MemberDto {

    /*Request*/
    @Builder
    public record CreateMemberRequest(
            Long id,
            @NotBlank(message = "Id를 입력해주세요")
            String loginId,
            @NotBlank(message = "password를 입력해주세요")
            String password,
            @NotBlank(message = "회원 이름은 필수입니다.")
            @Size(min = 1, max = 10, message = "회원 이름은 1~10자 사이입니다.")
            String name,
            String gender,
            String city,
            String street,
            String zipcode
    ) {

        public Member toEntity() {
            return Member.builder()
                    .name(name)
                    .gender(gender)
                    .loginId(loginId)
                    .password(password)
                    .address(new Address(city, street, zipcode))
                    .build();
        }



    }

    @Builder
    public record ModifyMember(
            Long id,
            String name,
            String gender,
            Address address
    ) {
        public static ModifyMember toModifyMember(Member member) {
            return ModifyMember.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .gender(member.getGender())
                    .address(member.getAddress())
                    .build();
        }
    }


    /*Response*/
    @Builder
    public record MemberResponse(
            Long id,
            String name,
            String gender,
            Address address
    ) {
        public static MemberResponse of(Member member) {
            return MemberResponse.builder()
                    .id(member.getId())
                    .name(member.getName())
                    .gender(member.getGender())
                    .address(member.getAddress())
                    .build();
        }
    }


}
