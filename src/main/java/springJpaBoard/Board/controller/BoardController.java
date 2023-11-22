package springJpaBoard.Board.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.controller.requestdto.BoardForm;
import springJpaBoard.Board.controller.requestdto.SaveCheck;
import springJpaBoard.Board.controller.requestdto.UpdateCheck;
import springJpaBoard.Board.controller.responsedto.CommentResponseDto;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.BoardSearch;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;
import springJpaBoard.Board.service.dto.UpdateBoardDto;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

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
        List<Member> memberList = memberService.findMembers();

        List<MemberResponseDto> members = memberList.stream()
                .map(m -> new MemberResponseDto(m))
                .collect(toList());

        model.addAttribute("members", members);
        model.addAttribute("boardForm", new BoardForm());
        return "boards/writeBoardForm";
    }

    @PostMapping("/write")
    public String write(@Validated(SaveCheck.class) @ModelAttribute BoardForm boardForm, BindingResult result, @RequestParam("memberId") Long memberId) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            System.out.println("result = " + result.getAllErrors());
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
//    @GetMapping
//    public String list(@RequestParam(value =  "offset", defaultValue = "0") int offset,
//                       @RequestParam(value = "limit", defaultValue = "100") int limit, Model model) {
//        List<Board> board = boardService.findBoardsMember(offset, limit);
//        List<BoardDto> boards = board.stream()
//                .map(b -> new BoardDto(b))
//                .collect(Collectors.toList());
//        model.addAttribute("boards", boards);
//
//        return "boards/boardList";
//    }
    @GetMapping
    public String boardList(@ModelAttribute("boardSearch") BoardSearch boardSearch, Model model) {
        List<Board> boardList = boardService.findBoardSearch(boardSearch);

        List<BoardDto> boards = boardList.stream()
                .map(b -> new BoardDto(b))
                .collect(toList());

        model.addAttribute("boards", boards);
        
        return "boards/boardList";
    }

    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/{boardId}/detail")
    public String detail(@PathVariable Long boardId, Model model) {
        boardService.updateView(boardId); // views ++
        BoardDetailDto board = new BoardDetailDto(boardService.findOne(boardId));

        List<CommentResponseDto> comments = board.getComments();

        /* 댓글 관련 */
        if (comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }


        model.addAttribute("board", board);
        model.addAttribute("commentForm", new CommentResponseDto());

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
    public String updateBoard(@Validated(UpdateCheck.class) @ModelAttribute BoardForm boardForm, @PathVariable Long boardId) {
        UpdateBoardDto boardDto = new UpdateBoardDto(boardId, boardForm.getTitle(), boardForm.getContent(), LocalDateTime.now());

        boardService.update(boardDto);

        return "redirect:/boards"; //게시글 수정 후 게시글 목록으로 이동
    }

    /**
     * 게시글 삭제
     */
    @GetMapping("/{boardId}/delete")
    public String deleteBoard(@PathVariable Long boardId){
        boardService.delete(boardId);

        return "redirect:/boards";
    }

    /**
     * BoardDto는 Controller에서만 사용하기 때문에 static class로 작성해줬다.
     */
    @Data
    static class BoardDto {
        private Long id;

        private String name;

        private String title;

        private String writer;

        private int view;

        private LocalDateTime boardDateTime;

        private int commentCount;

        public BoardDto(Board board) {
            this.id = board.getId();
            this.name = board.getMember().getName();
            this.title = board.getTitle();
            this.writer = board.getWriter();
            this.view = board.getView();
            this.boardDateTime = board.getBoardDateTime();
            this.commentCount = board.getCommentList().size();
        }
    }

    @Data
    static class BoardDetailDto{
        private Long id;

        private String title;

        private String content;

        private String writer;

        private int view;

        private LocalDateTime boardDateTime;

        private LocalDateTime modifyDateTime;

        private List<CommentResponseDto> Comments;

        public BoardDetailDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writer = board.getWriter();
            this.view = board.getView();
            this.boardDateTime = board.getBoardDateTime();
            this.modifyDateTime = board.getModifyDateTime();
            this.Comments = board.getCommentList().stream()
                    .map(comment -> new CommentResponseDto(comment))
                    .collect(toList());
        }

    }

    @Data
    static class MemberResponseDto {
        private Long id;
        private String name;

        public MemberResponseDto(Member member) {
            this.id = member.getId();
            this.name = member.getName();
        }
    }


}
