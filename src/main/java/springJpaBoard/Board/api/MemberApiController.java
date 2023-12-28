package springJpaBoard.Board.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.controller.requestdto.MemberRequestDTO;
import springJpaBoard.Board.controller.requestdto.SaveCheck;
import springJpaBoard.Board.controller.responsedto.MemberResponseDTO;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Transactional(readOnly = true)
public class MemberApiController {

    private final MemberService memberService;
    private final BoardService boardService;

    @Transactional
    @PostMapping("/members")
    public ResponseEntity saveMember(@RequestBody @Validated(SaveCheck.class)MemberRequestDTO memberRequestDTO, BindingResult result) {
        if (result.hasErrors()) {
            ResponseEntity.badRequest();
        }

        Address address = new Address(memberRequestDTO.getCity(), memberRequestDTO.getStreet(), memberRequestDTO.getZipcode());
        Member member = new Member();
        member.createMember(memberRequestDTO, address);
        Long id = memberService.join(member);

        return ResponseEntity.ok(id);
    }

    @GetMapping("/members")
    public Result members(@PageableDefault(page = 0, size=9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> memberList = null;


//        if (memberSearch.searchIsEmpty()) {
//            memberList = memberService.memberList(pageable);
//        } else {
//            String memberName = memberSearch.getMemberName();
//            GenderStatus memberGender = memberSearch.getMemberGender();
//
//            if (memberGender == null) {
//                memberList = memberService.searchName(memberName, pageable);
//            } else {
//                memberList = memberService.searchAll(memberName, memberGender, pageable);
//            }
//        }
        memberList = memberService.memberList(pageable);

        List<MemberResponseDTO> members = memberList.stream().
                map(m -> new MemberResponseDTO(m)).
                collect(toList());
        return new Result(members);
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
