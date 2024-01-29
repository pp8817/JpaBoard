package springJpaBoard.Board.domain.comment.exception;

import springJpaBoard.Board.global.Error.exception.EntityNotFoundException;

public class CommentNotFoundException extends EntityNotFoundException {

    public CommentNotFoundException(Long target) {
        super(target + " is not found");
    }
}
