package springJpaBoard.Board.repository.dto;

import lombok.Getter;
import lombok.Setter;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.GenderStatus;

@Getter
@Setter
public class UpdateMemberDto {

    private String name;

    private GenderStatus gender;

    private Address address;

    public UpdateMemberDto(String name, GenderStatus gender, Address address) {
        this.name = name;
        this.gender = gender;
        this.address = address;
    }
}
