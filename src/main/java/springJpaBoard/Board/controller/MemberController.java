package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.controller.requestdto.MemberForm;
import springJpaBoard.Board.controller.responsedto.MemberResponseDto;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.repository.search.MemberSearch;
import springJpaBoard.Board.service.MemberService;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Controller
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     */
    @GetMapping("/new")
    public String createForm(Model model) {
        /**
         * 빈 껍데기인 MemberFrom 객체를 model에 담아서 가져가는 이유는 Validation의 기능을 사용하기 위해서이다.
         */
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute MemberForm memberForm, BindingResult result) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

        Member member = new Member();
        member.createMember(memberForm.getName(), memberForm.getGender(), address);

        memberService.join(member); //PK 생성

        return "redirect:/";
    }

    /**
     * 회원 목록
     */

    @GetMapping
    public String memberList(@ModelAttribute("memberSearch") MemberSearch memberSearch, Model model, @PageableDefault(page = 0, size=10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Member> memberList = null;

        if (searchIsEmpty(memberSearch)) {
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

        List<MemberResponseDto> members = memberList.stream()
                .map(m -> new MemberResponseDto(m))
                .collect(toList());

        int nowPage = memberList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1); //Math.max를 이용해서 start 페이지가 0이하로 되는 것을 방지
        int endPage = Math.min(nowPage + 5, memberList.getTotalPages()); //endPage가 총 페이지의 개수를 넘지 않도록

        model.addAttribute("members", members);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "members/memberList";
    }

    private static boolean searchIsEmpty(MemberSearch memberSearch) {
        return (memberSearch.getMemberName() == "" || memberSearch.getMemberName() == null) && memberSearch.getMemberGender() == null;
    }

    /**
     * 회원 수정
     */
    @GetMapping("{memberId}/edit")
    public String updateMemberForm(@PathVariable("memberId") Long memberId, Model model) {
        Member member = memberService.findOne(memberId);
        Address address = member.getAddress();

        MemberForm form = new MemberForm();
        form.createForm(member.getId(), member.getName(), member.getGender(),
                address.getCity(), address.getStreet(), address.getZipcode());
        model.addAttribute("form", form);
        return "members/updateMemberForm";
    }

    @PostMapping("{memberId}/edit")
    public String updateMember(@ModelAttribute("form") MemberForm form) {
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();

        MemberResponseDto memberDto = new MemberResponseDto();
        memberDto.UpdateBoard(form.getId(), form.getName(), form.getGender(), address);
        memberService.update(memberDto);

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

}
