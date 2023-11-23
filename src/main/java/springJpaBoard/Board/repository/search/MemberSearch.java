package springJpaBoard.Board.repository.search;

import lombok.Getter;
import lombok.Setter;
import springJpaBoard.Board.domain.status.GenderStatus;

@Setter
@Getter
public class MemberSearch {
    private String memberName;
    private GenderStatus memberGender; //성별 [MAN, WOMAN, NEUTRALITY]
}
