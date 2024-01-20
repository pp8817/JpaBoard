package springJpaBoard.Board.domain.member.exception;

import springJpaBoard.Board.global.Error.exception.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    public MemberNotFoundException(Long target) {
        super(target + " is not found");
    }
}
