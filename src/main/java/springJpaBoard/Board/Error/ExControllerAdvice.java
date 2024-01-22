package springJpaBoard.Board.Error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springJpaBoard.Board.Error.exception.UserException;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExControllerAdvice {

    /**
     * 불법적이거나 부적절한 시간에 메서드가 호출되었음을 나타냅니다.
     * @Controller 혹은
     * @RestController 연결한 path에 중복이 있을 수 있다
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 상태 코드 400으로 응답
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResult illegalExHandle(IllegalStateException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    /**
     *  @ResponseStatus는 애노테이션이므로 HTTP 응답 코드를 동적으로 변경할 수 없다.
     *
     *  유저 관련 오류가 발생한 것을 나타냅니다.
     **/
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResult userExHandle(UserException e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("UserException", e.getMessage());
    }

    /* 최상위 부모 예외 */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return new ErrorResult("EX", "최상위 부모 예외");
    }
}
