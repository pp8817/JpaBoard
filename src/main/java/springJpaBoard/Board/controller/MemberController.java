package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.SesstionConst;
import springJpaBoard.Board.controller.requestdto.LoginCheck;
import springJpaBoard.Board.controller.requestdto.MemberRequestDTO;
import springJpaBoard.Board.controller.requestdto.SaveCheck;
import springJpaBoard.Board.controller.requestdto.UpdateCheck;
import springJpaBoard.Board.controller.responsedto.BoardResponseDTO;
import springJpaBoard.Board.controller.responsedto.MemberResponseDTO;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.repository.search.MemberSearch;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;

    /**
     * 회원 가입
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        /**
         * 빈 껍데기인 MemberFrom 객체를 model에 담아서 가져가는 이유는 Validation의 기능을 사용하기 위해서이다.
         */
        model.addAttribute("memberForm", new MemberRequestDTO());
        return "members/createMemberForm";
    }


    @PostMapping("/new")
    public String create(@Validated(SaveCheck.class) @ModelAttribute("memberForm") MemberRequestDTO memberForm, BindingResult result) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.createMember(memberForm, address);

        memberService.join(member); //PK 생성

        return "redirect:/";
    }

    /**
     * 회원 로그인, 로그아웃
     */
    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new MemberRequestDTO());
        return "members/loginMemberForm";
    }

    @PostMapping("/login")
    public String loginV4(@Validated(LoginCheck.class)
                        @ModelAttribute("loginForm") MemberRequestDTO form, BindingResult result, @RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {

        if (result.hasErrors()) {
            return "members/loginMemberForm";
        }

        Member loginMember = memberService.login(form.getLoginId(), form.getPassword());

        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "members/loginMemberForm";
        }

        //로그인 성공 처리

        /*세션이 있으면 있는 세션 반환, 없으면 신규 세션 생성*/
        HttpSession session = request.getSession();
        /*세션에 로그인 회원 정보 보관*/
        session.setAttribute(SesstionConst.LOGIN_MEMBER, loginMember);

        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        /*세션 삭제*/
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); //세션과 그 안의 데이터가 날아감
        }
        return "redirect:/";
    }


    /**
     * 회원 목록
     */
    @GetMapping
    public String memberList(@ModelAttribute("memberSearch") MemberSearch memberSearch, Model model, @PageableDefault(page = 0, size=9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> memberList = null;

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

        List<MemberResponseDTO> members = memberList.stream()
                .map(m -> new MemberResponseDTO(m))
                .collect(toList());

        int nowPage = memberList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1); //Math.max를 이용해서 start 페이지가 0이하로 되는 것을 방지
        int endPage = Math.min(nowPage + 5, memberList.getTotalPages()); //endPage가 총 페이지의 개수를 넘지 않도록
        int totalPages = memberList.getTotalPages();

        model.addAttribute("members", members);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "members/memberList";
    }

    /**
     * 회원 수정
     */
    @GetMapping("{memberId}/edit")
    public String updateMemberForm(@PathVariable("memberId") Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        Address address = member.getAddress();

        MemberRequestDTO form = new MemberRequestDTO();
        form.createForm(member.getId(), member.getName(), member.getGender(),
                address.getCity(), address.getStreet(), address.getZipcode());
        model.addAttribute("form", form);
        return "members/updateMemberForm";
    }

    @PostMapping("{memberId}/edit")
    public String updateMember(@Validated(UpdateCheck.class) @ModelAttribute("form") MemberRequestDTO form,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        MemberResponseDTO memberDto = new MemberResponseDTO();
        memberDto.update(form.getId(), form.getName(), form.getGender(), address);
        Member member = memberService.findOne(form.getId());
        memberService.update(member, memberDto);

        return "redirect:/members"; //회원 수정 후 회원 목록으로 이동
    }

    /**
     * 회원 삭제
     */
    @GetMapping("{memberId}/delete")
    public String deleteMember(@PathVariable Long memberId) {
        memberService.delete(memberId);

        return "redirect:/members";
    }

    /**
     * 회원이 작성한 게시글 리스트
     */
    @GetMapping("/myPosts")
    public String boardList(Model model, @Login Member loginMember) {
        Long id = loginMember.getId();
        Member member = memberService.findOne(id);

        List<BoardResponseDTO> boards = member.getBoardList().stream()
                .map(b -> new BoardResponseDTO(b))
                .collect(toList());

        System.out.println("boards = " + boards);

        model.addAttribute("name", member.getName());
        model.addAttribute("boards", boards);
        return "members/myPosts";
    }
}
