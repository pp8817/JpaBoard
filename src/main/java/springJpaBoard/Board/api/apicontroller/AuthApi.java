package springJpaBoard.Board.api.apicontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.Error.Message;
import springJpaBoard.Board.Error.StatusEnum;
import springJpaBoard.Board.Error.exception.UserException;
import springJpaBoard.Board.SessionConst;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.charset.Charset;

import static springJpaBoard.Board.controller.memberdto.AuthDto.LoginRequest;
import static springJpaBoard.Board.controller.memberdto.MemberDto.CreateMemberRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class AuthApi {

    private final MemberService memberService;

    /* 회원가입 */
    @PostMapping
    public ResponseEntity join(@RequestBody @Validated final CreateMemberRequest memberRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new UserException("회원가입: 회원 정보 입력 오류");
        }

        final Long id = memberService.join(memberRequestDTO);

        final Message message = new Message(StatusEnum.CREATED, "회원 가입 성공", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.CREATED);
    }

    /* 회원 로그인 */
    // TODO - redirectURL 해결
    // 현재 생각한 방법: Result 타입에 redirectURL을 추가해서 같이 반환
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid final LoginRequest loginRequest, BindingResult result,
                                @RequestParam(defaultValue = "/") final String redirectURL, HttpServletRequest request) {
        if (result.hasErrors()) {
            throw new UserException("로그인: 아이디 또는 비밀번호 오류");
        }

        final Member loginMember = memberService.login(loginRequest.loginId(), loginRequest.password());

        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            throw new UserException("로그인: 아이디 또는 비밀번호 오류");
        }


        /*세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성*/
        HttpSession session = request.getSession();
        /*세션에 로그인 회원 정보 보관*/
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        final Message message = new Message(StatusEnum.OK, "로그인 성공", loginMember.getLoginId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 회원 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<Message> logout(HttpServletRequest request) {
        /*세션 삭제*/
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); //세션 강제 종료

            final Message message = new Message(StatusEnum.OK, "회원 로그아웃 성공");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }

        throw new IllegalStateException("세션이 존재하지 않습니다.");
    }
}
