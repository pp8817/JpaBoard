package springJpaBoard.Board.repository.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMemberDto {

    private String name;

    private String gender;

    private String city;

    private String street;

    private String zipcode;
}
