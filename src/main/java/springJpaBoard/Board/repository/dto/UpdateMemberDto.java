package springJpaBoard.Board.repository.dto;

import lombok.Getter;
import lombok.Setter;
import springJpaBoard.Board.domain.Address;

@Getter
@Setter
public class UpdateMemberDto {

    private String name;

    private String gender;

    private Address address;
}
