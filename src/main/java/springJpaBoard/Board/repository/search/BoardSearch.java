package springJpaBoard.Board.repository.search;

import lombok.Getter;
import lombok.Setter;
import springJpaBoard.Board.domain.status.GenderStatus;

@Setter
@Getter
public class BoardSearch {

    private GenderStatus memberGender; //회원 성별
    private String boardTitle; //게시판 이름

    public boolean searchIsEmpty() {
        return (this.boardTitle == "" || this.boardTitle == null) && this.memberGender == null;
    }
}
