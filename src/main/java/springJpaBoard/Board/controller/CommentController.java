package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springJpaBoard.Board.controller.responsedto.CommentResponseDto;
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
    @PostMapping("/board/{id}/comment")
    public String saveComment(@Valid @ModelAttribute CommentResponseDto commentForm, BindingResult result, @PathVariable Long id) {

        if (result.hasErrors()) {
            System.out.println("유효성 검증 실패:");
            for (FieldError error : result.getFieldErrors()) {
                System.out.println("Field: " + error.getField() + ", Code: " + error.getCode() + ", Message: " + error.getDefaultMessage());
            }
            return "redirect:/boards/" + id + "/detail";
        }

        Board board = boardService.findOne(id);
        Comment comment = new Comment();
        comment.createComment(commentForm);

        comment.setBoard(board);
        commentService.save(comment);

        return "redirect:/boards/" + id + "/detail";
    }
}
