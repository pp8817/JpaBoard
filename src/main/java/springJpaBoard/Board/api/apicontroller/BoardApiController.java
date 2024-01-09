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
import springJpaBoard.Board.Error.exception.UserException;
import springJpaBoard.Board.api.apirepository.BoardApiRepository;
import springJpaBoard.Board.controller.requestdto.BoardRequestDTO;
import springJpaBoard.Board.controller.requestdto.SaveCheck;
import springJpaBoard.Board.controller.requestdto.UpdateCheck;
import springJpaBoard.Board.controller.responsedto.CommentResponseDTO;
import springJpaBoard.Board.controller.responsedto.MemberResponseDTO;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
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
    private final BoardApiRepository boardApiRepository;

    /* 게시글 작성 */
    @GetMapping
    public ResponseEntity writeForm(@Login Member loginMember) {

        MemberResponseDTO member = new MemberResponseDTO(loginMember);
//        BoardForm boardForm = new BoardForm();
//        boardForm.setMember(member);

        return ResponseEntity.status(HttpStatus.OK).body(member.getId());
    }


    @PostMapping
    public ResponseEntity<Message> write(@RequestBody @Validated(SaveCheck.class) BoardRequestDTO boardRequestDTO, @Login Member loginMember, BindingResult result) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            throw new IllegalStateException("게시글 작성: 양식을 맞춰주세요.");
        }

        Long boardId = boardService.write(boardRequestDTO, loginMember.getId());

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
            String memberGender = boardSearch.getMemberGender();

            if (memberGender == null) {
                boardList = boardService.searchTitle(boardTitle, pageable);
            } else {
                boardList = boardService.searchAll(boardTitle, memberGender, pageable);
            }
        }

        List<BoardDto> boards = boardList.stream()
                .map(b -> new BoardDto(b))
                .collect(toList());

        Message message = new Message(StatusEnum.OK, "게시글 목록 조회 성공", boards);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 게시글 상세 */
    @GetMapping("/detail/{boardId}")
    public ResponseEntity<Message> detail(@PathVariable Long boardId, @PageableDefault(page = 0, size = 10, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable) {
        boardService.updateView(boardId); // views ++
        Board board = boardService.findOne(boardId);
        BoardDetailDto boardDto = new BoardDetailDto(board);

        Message message = new Message(StatusEnum.OK, "게시글 상세 페이지 조회 성공", boardDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 게시글 수정 */
    @GetMapping("/edit/{boardId}")
    public ResponseEntity updateBoardForm(@PathVariable("boardId") Long boardId,
                                                   @Login Member loginMember) {
        Board board = boardApiRepository.findBoardWithMember(boardId);
        Member boardMember = board.getMember();

        if (memberService.loginValidation(loginMember, boardMember)) {
            BoardRequestDTO boardRequestDTO = new BoardRequestDTO();
            boardRequestDTO.createForm(board.getId(), board.getTitle(), board.getContent(), board.getWriter());

            Message message = new Message(StatusEnum.OK, "게시글 수정 페이지", boardRequestDTO);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }

        throw new UserException("게시글 회원 정보와 로그인 회원 정보가 일치하지 않습니다.");
    }

    @PutMapping("/edit/{boardId}")
    public ResponseEntity updateBoard(@RequestBody @Validated(UpdateCheck.class) BoardRequestDTO boardRequestDTO, BindingResult result,
                              @PathVariable Long boardId, @Login Member loginMember) {

        if (result.hasErrors()) {
            throw new IllegalStateException("양식을 지켜주세요.");
        }

        Board board = boardApiRepository.findBoardWithMember(boardId);
        Member boardMember = board.getMember();

        if (memberService.loginValidation(loginMember, boardMember)) {
            boardService.update(board, boardRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body("게시글 수정 성공");
        }

        throw new UserException("게시글 회원 정보와 로그인 회원 정보 불일치");
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/delete/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable Long boardId, @Login Member loginMember){
        //세션에 회원 데이터가 없으면 home
        Board board = boardApiRepository.findBoardWithMember(boardId);
        Member boardMember = board.getMember();
        if (memberService.loginValidation(loginMember, boardMember)) {
            boardService.delete(boardId);

            Message message = new Message(StatusEnum.OK, "게시글 삭제 성공", boardId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }

        throw new UserException("게시글 회원 정보와 로그인 회원 정보 불일치");
    }


    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
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

        private List<CommentResponseDTO> comments;

        public BoardDetailDto(Board board) {
            this.id = board.getId();
            this.title = board.getTitle();
            this.content = board.getContent();
            this.writer = board.getWriter();
            this.boardDateTime = board.getBoardDateTime();
            this.likes = board.getLikes();
            this.comments = board.getCommentList().stream()
                    .map(c -> new CommentResponseDTO(c))
                    .collect(toList());
        }
    }

}
