package springJpaBoard.Board.domain.member.api;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.model.MemberSearch;
import springJpaBoard.Board.domain.member.service.MemberService;
import springJpaBoard.Board.global.Error.Message;
import springJpaBoard.Board.global.Error.StatusEnum;
import springJpaBoard.Board.global.argumenresolver.Login;

import java.nio.charset.Charset;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static springJpaBoard.Board.domain.board.dto.BoardDto.MyPostsResponse;
import static springJpaBoard.Board.domain.member.dto.MemberDto.MemberResponse;
import static springJpaBoard.Board.domain.member.dto.MemberDto.ModifyMemberRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberApiController {

    private final MemberService memberService;
    private final BoardService boardService;

    /* 회원 목록 */
    @GetMapping
    public ResponseEntity<Message> members(@RequestBody final MemberSearch memberSearch, @PageableDefault(page = 0, size=9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        final Page<Member> memberList = memberListSearch(memberSearch, pageable);

        final List<MemberResponse> members = memberList.stream().
                map(MemberResponse::of).
                collect(toList());

        final Message message = new Message(StatusEnum.OK, "회원 목록 조회 성공", members);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }


    /* 회원 수정 */@GetMapping("/edit/{memberId}")
    public ResponseEntity updateForm(@PathVariable final Long memberId, @Login final Member loginMember) {

        final Member member = memberService.findOne(memberId);

        memberService.loginValidation(loginMember, member);

        final Message message = new Message(StatusEnum.OK, "회원 데이터 조회 성공", ModifyMemberRequest.of(member));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    @PutMapping("/edit/{memberId}")
    public ResponseEntity updateMember(@RequestBody @Validated final ModifyMemberRequest modifyMemberRequest, @Login final Member loginMember,
                                       @PathVariable final Long memberId) {

        final Member member = memberService.findOne(memberId);

        memberService.loginValidation(loginMember, member);
        final MemberResponse memberResponse = memberService.update(memberId, modifyMemberRequest);

        final Message message = new Message(StatusEnum.OK, "회원 정보 수정 성공", memberResponse);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 회원 삭제 */
    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity deleteMember(@PathVariable final Long memberId, @Login final Member loginMember) {
        final Member member = memberService.findOne(memberId);

        memberService.loginValidation(member, loginMember);
        memberService.delete(memberId);

        return ResponseEntity.status(HttpStatus.OK).body("회원 삭제 성공");
    }

    /**
     * 회원이 작성한 게시글 리스트
     */
    @GetMapping("/myPosts")
    public ResponseEntity boardList(@Login final Member loginMember) {

        final Member member = memberService.findOne(loginMember.getId());

        final List<MyPostsResponse> myPostsResponses = member.getBoardList().stream()
                .map(MyPostsResponse::of)
                .collect(toList());

        final Message message = new Message(StatusEnum.OK, "회원이 작성한 게시글 조회 성공", myPostsResponses);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    private Page<Member> memberListSearch(final MemberSearch memberSearch, Pageable pageable) {
        Page<Member> memberList = null;
        if (memberSearch.searchIsEmpty()) {
            memberList = memberService.memberList(pageable);
        } else {
            String memberName = memberSearch.getMemberName();
            String memberGender = memberSearch.getMemberGender();

            if (memberGender == null) {
                memberList = memberService.searchName(memberName, pageable);
            } else {
                memberList = memberService.searchAll(memberName, memberGender, pageable);
            }
        }
        return memberList;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

}
