package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttribute;
import springJpaBoard.Board.SesstionConst;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.LikeService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final BoardService boardService;

    @GetMapping("up/{boardId}")
    public String addLike(@PathVariable Long boardId, @SessionAttribute(name = SesstionConst.LOGIN_MEMBER, required = false) Member loginMember) {

        Board board = boardService.findOne(boardId);
        likeService.addLike(loginMember, board);

        return "redirect:/";
    }
}
