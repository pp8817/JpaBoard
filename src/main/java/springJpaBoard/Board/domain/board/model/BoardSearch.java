package springJpaBoard.Board.domain.board.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardSearch {

    private String memberGender; //회원 성별
    private String boardTitle; //게시판 이름

    public boolean searchIsEmpty() {
        return (this.boardTitle == "" || this.boardTitle == null) && this.memberGender == null;
    }
}
