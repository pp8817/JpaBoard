package springJpaBoard.Board.domain.member.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberSearch {
    private String memberName;
    private String memberGender; //성별 [남성, 여성, 중성, EX]

    public boolean searchIsEmpty() {
        return (this.memberName == "" || this.memberName == null) && this.memberGender == null;
    }

    public MemberSearch() {
    }
}
