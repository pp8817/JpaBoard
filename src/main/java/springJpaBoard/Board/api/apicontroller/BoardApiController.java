package springJpaBoard.Board.api.apicontroller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.Error.Message;
import springJpaBoard.Board.Error.StatusEnum;
import springJpaBoard.Board.controller.requestdto.BoardRequestDTO;
import springJpaBoard.Board.controller.requestdto.SaveCheck;
import springJpaBoard.Board.controller.responsedto.BoardResponseDTO;
import springJpaBoard.Board.controller.responsedto.CommentResponseDTO;
import springJpaBoard.Board.controller.responsedto.MemberResponseDTO;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.domain.status.GenderStatus;
import springJpaBoard.Board.repository.search.BoardSearch;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@Slf4j
public class BoardApiController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    /* 게시글 작성 */
    @GetMapping
    public ResponseEntity writeForm(@Login Member loginMember) {

        MemberResponseDTO member = new MemberResponseDTO(loginMember);
//        BoardForm boardForm = new BoardForm();
//        boardForm.setMember(member);

        return ResponseEntity.status(HttpStatus.OK).body(member.getId());
    }


    @PostMapping
    public ResponseEntity<Message> write(@RequestBody@Validated(SaveCheck.class) BoardRequestDTO boardRequestDTO, @Login Member loginMember, BindingResult result) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            throw new IllegalStateException("게시글 작성: 양식을 맞춰주세요.");
        }
        Board board = new Board();
        board.createBoard(boardRequestDTO.getTitle(), boardRequestDTO.getContent(), boardRequestDTO.getWriter(), LocalDateTime.now());
        Long boardId = boardService.write(board, loginMember.getId());

        Message message = new Message(StatusEnum.OK, "게시글 작성 성공", boardId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 게시글 목록 */
    @GetMapping("/list")
    public ResponseEntity<Message> boardLost(@RequestBody BoardSearch boardSearch, @PageableDefault(page = 0, size = 9, sort = "id", direction = Direction.ASC) Pageable pageable) {

        Page<Board> boardList = null;

        if (boardSearch.searchIsEmpty()) {
            boardList = boardService.boardList(pageable);
        } else {
            String boardTitle = boardSearch.getBoardTitle();
            GenderStatus memberGender = boardSearch.getMemberGender();

            if (memberGender == null) {
                boardList = boardService.searchTitle(boardTitle, pageable);
            } else {
                boardList = boardService.searchAll(boardTitle, memberGender, pageable);
            }
        }

        List<BoardDto> boards = boardList.stream()
                .map(b -> new BoardDto(b))
                .collect(toList());

        Message message = new Message(StatusEnum.OK, "게시글 작성 성공", boards);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 게시글 상세 */
    @GetMapping("/{boardId}/detail")
    public DetailResult detail(@PathVariable Long boardId, @PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        boardService.updateView(boardId); // views ++
        Board board = boardService.findOne(boardId);
        BoardResponseDTO boardDto = new BoardResponseDTO(board);
        //TODO 이 부분에서  commentList도 강제 Lazy로 땡겨오기, DTO 새로 만들면 됨 아래아래에 있느 코드를 DTO에 추가
        // 코드 변경 뒤에 JPA에서 DTO를 직접 가져오는 방식으로 변경?

        Page<Comment> commentList = commentService.getCommentsByBno(boardId, pageable);

        List<CommentResponseDTO> comments = commentList.stream()
                .map(CommentResponseDTO::new)
                .collect(toList());

        return new DetailResult(boardDto, comments); /* 두개 한 번에 DTO로 묶어서 보내기, 조회도 한 번에 가능 */
    }

    @Data
//    @AllArgsConstructor
    static class DetailResult<T> {
        private T data;
        private T comments;

        public DetailResult(T data, T comments) {
            this.data = data;
            this.comments = comments;
        }
    }

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
            this.name = board.getMember().getName(); //TODO fecth join으로 가져오도록 수정
            this.title = board.getTitle();
            this.writer = board.getWriter();
            this.view = board.getView();
            this.boardDateTime = board.getBoardDateTime();
            this.commentCount = board.getCommentCount();
        }
    }
}
