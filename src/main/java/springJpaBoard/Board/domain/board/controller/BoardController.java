package springJpaBoard.Board.domain.board.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.board.model.BoardSearch;
import springJpaBoard.Board.domain.board.repository.BoardApiRepository;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.comment.service.CommentService;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.service.MemberService;
import springJpaBoard.Board.global.argumenresolver.Login;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static springJpaBoard.Board.domain.board.dto.BoardDto.*;
import static springJpaBoard.Board.domain.comment.dto.CommentDto.CommentResponse;
import static springJpaBoard.Board.domain.comment.dto.CommentDto.CreateCommentRequest;
import static springJpaBoard.Board.domain.member.dto.MemberDto.MemberResponse;

@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final BoardApiRepository boardApiRepository;

    /**
     * 게시글 작성
     */
    @GetMapping("/write")
    public String write(@Login Member loginMember, Model model, HttpServletRequest request) {

        model.addAttribute("member", MemberResponse.of(loginMember));
        model.addAttribute("boardForm", CreateBoardRequest.builder().build());
        return "boards/writeBoardForm";
    }


    @PostMapping("/write")
    public String write(@Validated @ModelAttribute CreateBoardRequest boardRequestDTO, BindingResult result, @RequestParam("memberId") Long memberId) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            System.out.println("result = " + result.getAllErrors());
            return "boards/writeBoardForm";
        }

        boardService.write(boardRequestDTO, memberId);
        return "redirect:/";
    }

    /**
     * 게시글 목록
     */
    @GetMapping
    public String boardList(@ModelAttribute("boardSearch") BoardSearch boardSearch, Model model, @PageableDefault(page = 0, size=9, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Board> boardList = null;

        if (boardSearch.searchIsEmpty()) {
            boardList = boardService.boardList(pageable);
        } else {
            String boardTitle = boardSearch.getBoardTitle();
            String memberGender = boardSearch.getMemberGender();

            if (memberGender == "") {
                boardList = boardService.searchTitle(boardTitle, pageable);
            } else {
                boardList = boardService.searchAll(boardTitle, memberGender, pageable);
            }
        }

        List<BoardDto> boards = boardList.stream()
                .map(b -> new BoardDto(b))
                .collect(toList());

        int nowPage = boardList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1); //Math.max를 이용해서 start 페이지가 0이하로 되는 것을 방지
        int endPage = Math.min(nowPage + 5, boardList.getTotalPages()); //endPage가 총 페이지의 개수를 넘지 않도록
        int totalPages = boardList.getTotalPages();

        model.addAttribute("boards", boards);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("totalPages", totalPages);

        return "boards/boardList";

    }

    /**
     * 게시글 상세 페이지
     */
    @GetMapping("/{boardId}/detail")
    public String detail(@PathVariable Long boardId, @PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable,Model model) {
        boardService.updateView(boardId); // views ++
        Board board = boardService.findOne(boardId); //이때 comments도 담아오게?

        BoardDetailDto boardDto = new BoardDetailDto(board);

        /* 댓글 관련 */
        if (boardDto.comments != null && !boardDto.comments.isEmpty()) {
            model.addAttribute("comments", boardDto.comments);
        }

//        int nowPage = commentList.getPageable().getPageNumber() + 1;
//        int startPage = Math.max(nowPage - 4, 1); //Math.max를 이용해서 start 페이지가 0이하로 되는 것을 방지
//        int endPage = Math.min(nowPage + 5, commentList.getTotalPages()); //endPage가 총 페이지의 개수를 넘지 않도록
//        int totalPages = commentList.getTotalPages();
//
//        model.addAttribute("nowPage", nowPage);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        model.addAttribute("totalPages", totalPages);

        model.addAttribute("board", boardDto);
//        model.addAttribute("member", member);
        model.addAttribute("commentForm", CreateCommentRequest.builder().build());

        return "boards/boardDetail";
    }

    /**
     * 게시글 수정
     */
    @GetMapping("/{boardId}/edit")
    public String updateBoardForm(@PathVariable("boardId") Long boardId, Model model,
                                  @Login Member loginMember) {
//        Board board = boardService.findOne(boardId);
        Board board = boardApiRepository.findBoardWithMember(boardId);
        Member boardMember = board.getMember();


        memberService.loginValidation(loginMember, boardMember);

        ModifyBoardResponse modifyBoardResponse = ModifyBoardResponse.of(board);
        model.addAttribute("boardForm", modifyBoardResponse);

        return "/boards/updateBoardForm";

    }

    @PostMapping("/{boardId}/edit")
    public String updateBoard(@Validated @ModelAttribute ModifyBoardRequest boardRequestDTO,
                              @PathVariable Long boardId, @Login Member loginMember) {

        Board board = boardApiRepository.findBoardWithMember(boardId);
        Member boardMember = board.getMember();

        memberService.loginValidation(loginMember, boardMember);
        boardService.update(board, boardRequestDTO);
        return "redirect:/boards/" + boardId + "/detail"; //게시글 수정 후 게시글 목록으로 이동
    }

    /**
     * 게시글 삭제
     */
    @GetMapping("/{boardId}/delete")
    public String deleteBoard(@PathVariable Long boardId, @Login Member loginMember){
        //세션에 회원 데이터가 없으면 home
        Board board = boardService.findOne(boardId);
        Member boardMember = board.getMember();

        memberService.loginValidation(loginMember, boardMember);
        boardService.delete(boardId);

        return "redirect:/boards";
    }

    /**
     * BoardDto는 Controller에서만 사용하기 때문에 static class로 작성해줬다.
     */
    @Data
    @AllArgsConstructor
    static class BoardDto {
        private Long id;

        /*회원 이름은 나중에 회원 이름 변경 기능이 생긴다면 문제가 될 수 있기 때문에 일단 변경 x*/
        private String name;

        private String title;

        private String writer;

        private int view;

        private LocalDateTime boardDateTime;

        /*댓글을 작성할 때마다 board의 댓글 수를 증가하는 방식으로*/
        private int commentCount;

        public BoardDto(Board board) {
            this.id = board.getId();
            this.name = board.getMember().getName();
            this.title = board.getTitle();
            this.writer = board.getWriter();
            this.view = board.getView();
            this.boardDateTime = board.getBoardDateTime();
            this.commentCount = board.getCommentCount();
        }
    }

    @Data
    @AllArgsConstructor
    static class BoardDetailDto {
        private Long id;

        private String title;

        private String content;

        private String writer;

        private int likes;

        private LocalDateTime boardDateTime;

        private List<CommentResponse> comments;

        public BoardDetailDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writer = board.getWriter();
            this.boardDateTime = board.getBoardDateTime();
            this.likes = board.getLikes();
            this.comments = board.getCommentList().stream()
                    .map(CommentResponse::of)
                    .collect(toList());
        }
    }
}
