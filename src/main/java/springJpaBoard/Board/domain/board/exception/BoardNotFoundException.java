package springJpaBoard.Board.domain.board.exception;

import springJpaBoard.Board.global.Error.exception.EntityNotFoundException;

public class BoardNotFoundException extends EntityNotFoundException {

    public BoardNotFoundException(Long target) {
        super(target + " is not found");
    }
}
