package springJpaBoard.Board.domain.member.exception;

import springJpaBoard.Board.global.Error.exception.BusinessException;
import springJpaBoard.Board.global.Error.exception.ErrorCode;

public class AuthException extends BusinessException {
    public AuthException(ErrorCode code) {
        super(code);
    }
}
