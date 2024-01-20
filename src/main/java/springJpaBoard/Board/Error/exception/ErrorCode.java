package springJpaBoard.Board.Error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    //Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", " Invalid Input Value"),
    ENTITY_NOT_FOUND(400, "C003", " Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", " Invalid Type Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),

    //로그인,회원가입
    UNAUTHORIZED_USER(401, "A001", "로그인이 필요합니다."),
    INVALID_ACCESS_TOKEN(401, "A002", "유효하지 않은 access token입니다."),
    INVALID_REFRESH_TOKEN(401, "A003", "유효하지 않은 refresh token입니다."),
    USER_ALREADY_EXISTS(400, "A004", "이미 가입된 유저입니다."),
    NEED_TO_JOIN(400, "A005", "회원가입이 필요합니다."),


    //회원
    NICKNAME_DUPLICATION(409, "U001", "중복되는 닉네임입니다."),
    NOT_FOUND_USER(404, "U002", "존재하지 않는 회원입니다.");

    //Board

    //Comment

    //Board

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
