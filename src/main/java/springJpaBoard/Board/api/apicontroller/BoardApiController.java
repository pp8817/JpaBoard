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
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.repository.search.BoardSearch;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.Charset;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction;
import static springJpaBoard.Board.controller.boarddto.BoardDto.*;
import static springJpaBoard.Board.controller.commentdto.CommentDto.CommentResponse;
import static springJpaBoard.Board.controller.memberdto.MemberDto.MemberResponse;

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

        MemberResponse member = MemberResponse.of(loginMember);

        return ResponseEntity.status(HttpStatus.OK).body(member.id());
    }


    @PostMapping
    public ResponseEntity<Message> write(@RequestBody @Validated CreateBoardRequest boardRequestDTO, @Login Member loginMember, BindingResult result) {

        /*
        오류 발생시(@Valid 에서 발생)
         */
        if (result.hasErrors()) {
            throw new IllegalStateException("게시글 양식에 맞지 않습니다.");
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

        Page<Board> boardList = searchBoardList(boardSearch, pageable);

        List<BoardListResponse> boards = boardList.stream()
                .map(BoardListResponse::of)
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

        Board board = boardService.findOne(boardId);
        boardService.updateView(boardId); // views ++

        List<CommentResponse> comments = board.getCommentList().stream()
                .map(CommentResponse::of)
                .toList();

        BoardResponse boardDto = BoardResponse.of(board, comments);

        Message message = new Message(StatusEnum.OK, "게시글 상세 페이지 조회 성공", boardDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }

    /* 게시글 수정 */
    @GetMapping("/edit/{boardId}")
    public ResponseEntity updateBoardForm(@PathVariable("boardId") Long boardId,
                                                   @Login Member loginMember) {
        Board board = boardApiRepository.findBoardWithMember(boardId); //쿼리 최적화를 위해서 사용
        Member boardMember = board.getMember();

        if (memberService.loginValidation(loginMember, boardMember)) {

            ModifyBoardResponse modifyBoardResponse = ModifyBoardResponse.of(board);

            Message message = new Message(StatusEnum.OK, "게시글 수정 페이지", modifyBoardResponse);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        }

        throw new UserException("게시글 회원 정보와 로그인 회원 정보가 일치하지 않습니다.");
    }

    @PutMapping("/edit/{boardId}")
    public ResponseEntity updateBoard(@RequestBody ModifyBoardRequest boardRequestDTO, BindingResult result,
                                      @PathVariable Long boardId, @Login Member loginMember) {

        if (result.hasErrors()) {
            throw new IllegalStateException("양식을 지켜주세요.");
        }

        Board board = boardApiRepository.findBoardWithMember(boardId);
        Member boardMember = board.getMember();

        if (memberService.loginValidation(loginMember, boardMember)) {
            ModifyBoardResponse modifyBoardResponse = boardService.update(board, boardRequestDTO);

            Message message = new Message(StatusEnum.OK, "게시글 수정 성공", modifyBoardResponse);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return new ResponseEntity<>(message, headers, HttpStatus.OK);
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

    private Page<Board> searchBoardList(BoardSearch boardSearch, Pageable pageable) {
        Page<Board> boardList;
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
        return boardList;
    }
}
