package springJpaBoard.Board.service.dto;

import lombok.Getter;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.GenderStatus;

@Getter
public class UpdateMemberDto {

    private Long id;

    private String name;

    private GenderStatus gender;

    private Address address;


    public UpdateMemberDto(Long id, String name, GenderStatus gender, Address address) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
    }

    public UpdateMemberDto() {
    }
}
