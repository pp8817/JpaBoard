package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springJpaBoard.Board.controller.requestdto.CommentRequestDTO;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import javax.validation.Valid;

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
    public String saveComment(@Valid @ModelAttribute CommentRequestDTO commentRequestDTO, BindingResult result, @Login Member loginMember) {
        Long bno = commentRequestDTO.getBno();


        if (result.hasErrors()) {
            System.out.println("유효성 검증 실패:");
            for (FieldError error : result.getFieldErrors()) {
                System.out.println("Field: " + error.getField() + ", Code: " + error.getCode() + ", Message: " + error.getDefaultMessage());
            }
            return "redirect:/boards/" + bno + "/detail";
        }

        Board board = boardService.findOne(bno); //쿼리 1번
        if (board != null && memberService.findOne(loginMember.getId())!=null) {
            board.addComment();
            Comment comment = new Comment();
            comment.createComment(commentRequestDTO);

            comment.setBoard(board);
            commentService.save(comment, loginMember.getId());

            return "redirect:/boards/" + bno + "/detail"; //쿼리 1번, 게시글 상세 페이지를 다시 로딩하면서 board의 정보가 필요
        }
        return "redirect:/";
    }

    @GetMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        Long bno = commentService.findById(id).getBno();
        commentService.delete(id, bno);
        return "redirect:/boards/" + bno + "/detail";
    }
}
