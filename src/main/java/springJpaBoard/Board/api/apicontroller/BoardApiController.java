package springJpaBoard.Board.api.apicontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import springJpaBoard.Board.controller.responsedto.MemberResponseDTO;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
@Slf4j
public class BoardApiController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity write(@Login Member loginMember) {

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

}
