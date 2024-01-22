package springJpaBoard.Board.domain.like.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import springJpaBoard.Board.global.constans.SessionConst;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.like.service.LikeService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;
    private final BoardService boardService;

    @GetMapping("up/{boardId}")
    public String addLike(@PathVariable Long boardId, @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember) {

        Board board = boardService.findOne(boardId);
        likeService.addLike(loginMember, board);

        return "redirect:/";
    }
}