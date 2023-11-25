package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.controller.requestdto.CommentForm;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;


import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;


    /**
     * 댓글 작성
     */
    @PostMapping("/board/comment")
    public String saveComment(@Valid @ModelAttribute CommentForm commentForm, BindingResult result, @RequestParam("memberId") Long memberId) {
        Long bno = commentForm.getBno();
        System.out.println("memberId = " + memberId);

        if (result.hasErrors()) {
            System.out.println("유효성 검증 실패:");
            for (FieldError error : result.getFieldErrors()) {
                System.out.println("Field: " + error.getField() + ", Code: " + error.getCode() + ", Message: " + error.getDefaultMessage());
            }
            return "redirect:/boards/" + bno + "/detail";
        }

        Board board = boardService.findOne(bno); //쿼리 1번
        Comment comment = new Comment();
        comment.createComment(commentForm);

        comment.setBoard(board);
        commentService.save(comment, memberId);

        return "redirect:/boards/" + bno + "/detail"; //쿼리 1번, 게시글 상세 페이지를 다시 로딩하면서 board의 정보가 필요
    }

    @GetMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        Comment comment = commentService.findById(id);
        commentService.delete(id);
        return "redirect:/boards/" + comment.getBno() + "/detail";
    }
}
