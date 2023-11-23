package springJpaBoard.Board.repository.search;

import lombok.Getter;
import lombok.Setter;
import springJpaBoard.Board.domain.status.GenderStatus;

@Setter
@Getter
public class BoardSearch {

    private GenderStatus MemberGender; //회원 성별
    private String BoardTitle; //게시판 이름
}
