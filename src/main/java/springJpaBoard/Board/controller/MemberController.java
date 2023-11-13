package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.controller.form.MemberForm;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.dto.UpdateMemberDto;
import springJpaBoard.Board.service.MemberService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
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
    public String create(@Valid @ModelAttribute MemberForm form, BindingResult result) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member();

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        member.createMember(form.getName(), form.getGender(), address);

        memberService.join(member);

        return "redirect:/";
    }

    /**
     * 회원 목록
     */
    @GetMapping
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
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
    public String updatemember(@ModelAttribute("form") MemberForm form) {
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        UpdateMemberDto memberDto = new UpdateMemberDto(form.getName(), form.getGender(), address);
        return "redirect:/members";
    }

    /**
     * 회원 삭제
     */
}
