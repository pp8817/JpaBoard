package springJpaBoard.Board.controller.responsedto;

import lombok.Getter;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.status.GenderStatus;

@Getter
public class MemberResponseDTO {

    private Long id;
    private String name;
    private GenderStatus gender;
    private Address address;

    public MemberResponseDTO(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.gender = member.getGender();
        this.address = member.getAddress();
    }

    public void UpdateBoard(Long id, String name, GenderStatus gender, Address address) {
        this.id=id;
        this.name=name;
        this.gender=gender;
        this.address=address;
    }

    public MemberResponseDTO() {
    }
}
