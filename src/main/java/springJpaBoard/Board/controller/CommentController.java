package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springJpaBoard.Board.controller.form.CommentDto;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final BoardService boardService;
    private final CommentService commentService;

    /**
     * 댓글 작성
     */
    @PostMapping("/board/{id}/comment")
    public String saveComment(@PathVariable Long id, @ModelAttribute CommentDto commentDto) {
        Board board = boardService.findOne(id);
        Comment comment = new Comment();
        comment.createComment(commentDto);

        /**
         * 연관관계를 설정하면서 각 board에 comment들이 저장됨.
         * html에서 board의 comment의 정보들을 꺼내서 나타내주면 됨
         */
        comment.setBoard(board);
        commentService.save(comment);

        return "redirect:/boards/" + id + "/detail";
    }
}
