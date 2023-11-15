package springJpaBoard.Board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.controller.form.BoardForm;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.MemberService;
import springJpaBoard.Board.service.dto.UpdateBoardDto;
import springJpaBoard.Board.service.BoardService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;

    /**
     * 게시글 작성
     */
    @GetMapping("/write")
    public String writeForm(Model model) {
        /**
         * 빈 껍데기인 MemberFrom 객체를 model에 담아서 가져가는 이유는 Validation의 기능을 사용하기 위해서이다.
         */
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        model.addAttribute("boardForm", new BoardForm());
        return "boards/writeBoardForm";
    }

    @PostMapping("/write")
    public String write(@Valid @ModelAttribute BoardForm boardForm, @RequestParam("memberId") Long memberId, BindingResult result) {


        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            return "boards/writeBoardForm";
        }
        Board board = new Board();
        board.createBoard(boardForm.getTitle(), boardForm.getContent(), boardForm.getWriter(), LocalDateTime.now());
        boardService.write(board, memberId);
        return "redirect:/";
    }

    /**
     * 게시글 목록
     */
    @GetMapping
    public String list(Model model) {
        List<Board> boards = boardService.findBoards();
        model.addAttribute("boards", boards);
        return "boards/boardList";
    }

    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/{boardId}/detail")
    public String detail(@PathVariable Long boardId, Model model) {
        Board board = boardService.findOne(boardId);
        model.addAttribute("board", board);
        System.out.println("board.getContent() = " + board.getContent());
        return "boards/boardDetail";
    }

    /**
     * 게시글 수정
     */
    @GetMapping("/{boardId}/edit")
    public String updateBoardForm(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.findOne(boardId);

        BoardForm boardForm = new BoardForm();
        boardForm.createForm(board.getId(), board.getTitle(), board.getContent(), board.getWriter());

        model.addAttribute("boardForm", boardForm);
        return "/boards/updateBoardForm";
    }

    @PostMapping("/{boardId}/edit")
    public String updateBoard(@Valid @ModelAttribute BoardForm boardForm, @PathVariable Long boardId) {
        System.out.println("boardForm.getWriter() = " + boardForm.getWriter());
        UpdateBoardDto boardDto = new UpdateBoardDto(boardId, boardForm.getTitle(), boardForm.getContent(), LocalDateTime.now());

        boardService.update(boardDto);

        return "redirect:/boards"; //게시글 수정 후 게시글 목록으로 이동
    }
}
