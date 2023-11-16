package springJpaBoard.Board.repository;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BoardSearch {

    private String MemberGender; //회원 성별
    private String BoardTitle; //게시판 이름
}
