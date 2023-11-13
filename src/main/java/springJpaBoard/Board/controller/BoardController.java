package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.controller.form.BoardForm;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.service.dto.UpdateBoardDto;
import springJpaBoard.Board.service.BoardService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시글 작성
     */
    @GetMapping("/write")
    public String writeForm(Model model) {
        /**
         * 빈 껍데기인 MemberFrom 객체를 model에 담아서 가져가는 이유는 Validation의 기능을 사용하기 위해서이다.
         */
        model.addAttribute("form", new BoardForm());
        return "boards/writeBoardForm";
    }

    @PostMapping("/write")
    public String write(@Valid @ModelAttribute BoardForm boardForm, BindingResult result) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Board board = new Board();
        board.createBoard(boardForm.getTitle(), board.getContent(), board.getWriter());
        boardService.write(board);
        return "redirect:/";
    }

    /**
     * 게시글 목록
     */
    @PostMapping
    public String list(Model model) {
        List<Board> boards = boardService.findBoards();
        model.addAttribute("boards", boards);
        return "boards/boardList";
    }

    /**
     * 게시글 수정
     */
    @GetMapping("/{boardId}/edit")
    public String updateBoardForm(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.findOne(boardId);

        BoardForm form = new BoardForm();
        form.createForm(board.getId(), board.getTitle(), board.getContent(), board.getWriter());

        model.addAttribute("form", form);
        return "/boards/updateBoardForm";
    }

    @PostMapping("/{boardId}/edit")
    public String updateBoard(@ModelAttribute("form") BoardForm form) {
        UpdateBoardDto boardDto = new UpdateBoardDto(form.getId(), form.getTitle(), form.getContent());

        boardService.update(boardDto);

        return "redirect:/boards"; //게시글 수정 후 게시글 목록으로 이동
    }
}
