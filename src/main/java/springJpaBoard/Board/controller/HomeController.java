package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import springJpaBoard.Board.SesstionConst;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.MemberRepository;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SesstionConst.LOGIN_MEMBER, required = false)
                                        Member loginMember, Model model) {

        //세션이 없으면 home
        if (loginMember == null) {
            return "home";
        }

        /*DTO로 감싸기!*/
        //세션이 유지되면 로그인 홈으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
