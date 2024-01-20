package springJpaBoard.Board.domain.member.exception;

import springJpaBoard.Board.global.Error.exception.BusinessException;
import springJpaBoard.Board.global.Error.exception.ErrorCode;

public class UserException extends BusinessException {
    public UserException(ErrorCode code) {
        super(code);
    }
}
