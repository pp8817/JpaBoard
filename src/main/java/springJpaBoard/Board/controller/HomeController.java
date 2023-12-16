package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.MemberRepository;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId,
                            Model model) {
        if (memberId == null) {
            return "home";
        }

        //로그인
        Optional<Member> loginMember = memberRepository.findById(memberId);
        if (loginMember.isEmpty()) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}
