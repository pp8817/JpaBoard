package springJpaBoard.Board.domain.etc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.repository.MemberRepository;
import springJpaBoard.Board.global.argumenresolver.Login;

import static springJpaBoard.Board.domain.member.dto.MemberDto.MemberResponse;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(@Login Member loginMember, Model model) {

        //세션이 없으면 home
        if (loginMember == null) {
            return "home";
        }

        /*DTO로 감싸기!*/
        //세션이 유지되면 로그인 홈으로 이동
        MemberResponse memberResponseDto = MemberResponse.of(loginMember);

        model.addAttribute("member", memberResponseDto);
        return "loginHome";
    }
}
