package springJpaBoard.Board.repository.search;

import lombok.Getter;
import lombok.Setter;
import springJpaBoard.Board.domain.status.GenderStatus;

import static springJpaBoard.Board.domain.status.GenderStatus.*;

@Setter
@Getter
public class MemberSearch {
    private String memberName;
    private GenderStatus memberGender = EX; //성별 [남성, 여성, 중성, EX]

    public boolean searchIsEmpty() {
        return (this.memberName == "" || this.memberName == null) && this.memberGender == null;
    }

    public MemberSearch() {
    }
}
