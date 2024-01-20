package springJpaBoard.Board.global.Error.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    //Common
    INVALID_INPUT_VALUE(400, "C001", "잘못된 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", "잘못된 입력값입니다."),
    ENTITY_NOT_FOUND(400, "C003", "존재하지 않는 엔티티입니다."),
    INTERNAL_SERVER_ERROR(500, "C004", "서버 오류"),
    INVALID_TYPE_VALUE(400, "C005", " 잘못된 타입입니다."),
    HANDLE_ACCESS_DENIED(403, "C006", "접근이 금지됐습니다."),

    //Login, Join
    UNAUTHORIZED_USER(401, "A001", "로그인이 필요합니다."),
    USER_ALREADY_EXISTS(400, "A002", "이미 가입된 유저입니다."),
    NEED_TO_JOIN(400, "A003", "회원가입이 필요합니다."),
    USER_MISMATCH(401, "A004", "회원 정보가 불일치합니다."),

    //Member
    NICKNAME_DUPLICATION(409, "U001", "중복되는 닉네임입니다."),
    NOT_FOUND_USER(404, "U002", "존재하지 않는 회원입니다."),
    INVALID_INPUT_ID_PASSWORD(400, "U003", "Id잘모 Password가 일치하지 않습니다."),;

    //Board

    //Comment

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
