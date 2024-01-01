package springJpaBoard.Board.api.apicontroller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.Error.Message;
import springJpaBoard.Board.Error.exception.UserException;
import springJpaBoard.Board.SesstionConst;
import springJpaBoard.Board.Error.StatusEnum;
import springJpaBoard.Board.controller.requestdto.LoginCheck;
import springJpaBoard.Board.controller.requestdto.MemberRequestDTO;
import springJpaBoard.Board.controller.requestdto.SaveCheck;
import springJpaBoard.Board.controller.requestdto.UpdateCheck;
import springJpaBoard.Board.controller.responsedto.MemberResponseDTO;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.repository.search.MemberSearch;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberApiController {

    private final MemberService memberService;
    private final BoardService boardService;

    /* 회원가입 */
    @PostMapping
    public ResponseEntity join(@RequestBody @Validated(SaveCheck.class) MemberRequestDTO memberRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            throw new UserException("회원가입: 회원 정보 입력 오류");
        }

        Address address = new Address(memberRequestDTO.getCity(), memberRequestDTO.getStreet(), memberRequestDTO.getZipcode());

        Member member = new Member();
        member.createMember(memberRequestDTO, address);
        Long id = memberService.join(member);

        Message message = new Message(StatusEnum.OK, "회원 가입 성공", id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));


        return new ResponseEntity<>(message, headers, HttpStatus.OK);
//        return ResponseEntity.status(HttpStatus.OK).body("성공");
    }

    /* 회원 로그인 */
    // TODO - redirectURL 해결
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated(LoginCheck.class) MemberRequestDTO form, BindingResult result,
                        @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {
        if (result.hasErrors()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디 또는 비밀번호 오류");
            throw new UserException("로그인: 아이디 또는 비밀번호 오류");
        }

        Member loginMember = memberService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 맞지 않습니다.");
            throw new UserException("로그인: 아이디 또는 비밀번호 오류");
        }

        //로그인 성공 처리

        /*세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성*/
        HttpSession session = request.getSession();
        /*세션에 로그인 회원 정보 보관*/
        session.setAttribute(SesstionConst.LOGIN_MEMBER, loginMember);

        return ResponseEntity.status(HttpStatus.OK).body("로그인 성공");
    }

    /* 회원 로그아웃 */
    @PostMapping("/logout")
    public ResponseEntity<Message> logout(HttpServletRequest request) {
        /*세션 삭제*/
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();

            Message message = new Message(StatusEnum.OK, "회원 목록 조회 성공");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }

//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        throw new IllegalStateException("로그아웃: 세션 오류");
    }

    /* 회원 목록 */
    // TODO - memberSearch의 Gender가 enum 타입이기 때문에 null 값으로 넘어오면 오류 발생
    @GetMapping
    public ResponseEntity<Message> members(@RequestBody MemberSearch memberSearch, @PageableDefault(page = 0, size=9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Member> memberList = null;
        log.info("memberName = {}, {}", memberSearch.getMemberGender(), memberSearch.getMemberName());


        if (memberSearch.searchIsEmpty()) {
            memberList = memberService.memberList(pageable);
        } else {
            String memberName = memberSearch.getMemberName();
            GenderStatus memberGender = memberSearch.getMemberGender();

            if (memberGender == null) {
                memberList = memberService.searchName(memberName, pageable);
            } else {
                memberList = memberService.searchAll(memberName, memberGender, pageable);
            }
        }

        List<MemberResponseDTO> members = memberList.stream().
                map(m -> new MemberResponseDTO(m)).
                collect(toList());

//        Result result = new Result(members);

        Message message = new Message(StatusEnum.OK, "회원 목록 조회 성공", members);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 회원 수정 */
    @GetMapping("/{memberId}/edit")
    public ResponseEntity updateForm(@PathVariable Long memberId) {

        Member member = memberService.findOne(memberId);

        ModifyMemberDto memberDto = new ModifyMemberDto(member);

        Result result = new Result(memberDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     *
     *  TODO 수정 안되는 오류 해결
     * */
    @PutMapping("{memberId}/edit")
    public ResponseEntity updateMember(@RequestBody @Validated(UpdateCheck.class) MemberRequestDTO form, BindingResult result) {

        if (result.hasErrors()) {
            throw new UserException("회원 수정 오류");
        }

        memberService.update(form.getId(), form);
//        ModifyMemberDto modifyMember = new ModifyMemberDto(updateMember);

        Message message = new Message(StatusEnum.OK, "회원 정보 수정 성공");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 회원 삭제 */
    @DeleteMapping("{memberId}/delete")
    public ResponseEntity deleteMember(@PathVariable Long memberId) {
        memberService.delete(memberId);

        return ResponseEntity.status(HttpStatus.OK).body("회원 삭제 성공");
    }

    /**
     * 회원이 작성한 게시글 리스트
     */
    @GetMapping("/myPosts")
    public ResponseEntity boardList(@Login Member loginMember) {
        Long id = loginMember.getId();
        Member member = memberService.findOne(id); // 1

        List<MyBoardsDto> boards = member.getBoardList().stream()
                .map(b -> new MyBoardsDto(b))
                .collect(toList());

        Message message = new Message(StatusEnum.OK, "회원이 작성한 게시글 조회 성공", boards);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class ModifyMemberDto {
        private Long id;
        private String name;
        private GenderStatus gender;
        private Address address;

        public ModifyMemberDto(Member member) {
            this.id = member.getId();
            this.name = member.getName();
            this.gender = member.getGender();
            this.address = member.getAddress();
        }
    }

    @Data
    @AllArgsConstructor
    static class MyBoardsDto {
        private Long id;
        private String title;
        private String writer;
        private LocalDateTime localDateTime;
        private int view;
        private int commentCount;

        public MyBoardsDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.writer = board.getWriter();
            this.localDateTime = board.getBoardDateTime();
            this.view = board.getView();
            this.commentCount = board.getCommentCount();
        }
    }
}
