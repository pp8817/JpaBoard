package springJpaBoard.Board.service.dto;

import lombok.Getter;
import springJpaBoard.Board.domain.Address;

@Getter
public class UpdateMemberDto {

    private Long id;

    private String name;

    private String gender;

    private Address address;


    public UpdateMemberDto(Long id, String name, String gender, Address address) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
    }

    public UpdateMemberDto() {
    }
}
