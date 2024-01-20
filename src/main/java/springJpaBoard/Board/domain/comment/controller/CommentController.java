package springJpaBoard.Board.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.comment.service.CommentService;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.service.MemberService;
import springJpaBoard.Board.global.argumenresolver.Login;

import javax.validation.Valid;

import static springJpaBoard.Board.domain.comment.dto.CommentDto.CreateCommentRequest;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final MemberService memberService;


    /**
     * 댓글 작성
     */
    @PostMapping("/comment")
    public String saveComment(@Valid @ModelAttribute CreateCommentRequest commentRequestDTO, BindingResult result, @Login Member loginMember) {
        Long bno = commentRequestDTO.bno();


        if (result.hasErrors()) {
            return "redirect:/boards/" + bno + "/detail";
        }

        commentService.save(commentRequestDTO, loginMember.getId());

        return "redirect:/boards/" + bno + "/detail"; //쿼리 1번, 게시글 상세 페이지를 다시 로딩하면서 board의 정보가 필요
    }

    @GetMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        Long bno = commentService.findOne(id).getBno();
        commentService.delete(id, bno);
        return "redirect:/boards/" + bno + "/detail";
    }
}
