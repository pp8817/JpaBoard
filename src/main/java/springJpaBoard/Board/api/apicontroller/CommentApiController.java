package springJpaBoard.Board.api.apicontroller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.Error.Message;
import springJpaBoard.Board.Error.StatusEnum;
import springJpaBoard.Board.controller.requestdto.CommentRequestDTO;
import springJpaBoard.Board.controller.responsedto.CommentResponseDTO;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Slf4j
public class CommentApiController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final MemberService memberService;

    /**
     * 댓글 작성
     */
    @PostMapping
    public ResponseEntity saveComment(@RequestBody CommentRequestDTO commentRequestDTO, BindingResult result, @Login Member loginMember) {

        Long bno = commentRequestDTO.getBno();

        if (result.hasErrors()) {
            throw new IllegalStateException("양식 불일치 오류");
        }

        Comment comment = commentService.save(commentRequestDTO, loginMember.getId());

        CommentResponseDTO commentDto = new CommentResponseDTO(comment);

        Message message = new Message(StatusEnum.OK, "댓글 작성 성공", commentDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId) {
        try {
            Comment comment = commentService.findById(commentId);
            Long bno = comment.getBno();
            commentService.delete(commentId, bno);

            Message message = new Message(StatusEnum.OK, "댓글 삭제 성공", commentId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return new ResponseEntity<>(message, headers, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            Message message = new Message(StatusEnum.OK, "해당 ID에 대한 댓글을 찾을 수 없습니다.", commentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
    }

    @Getter
    static class CommentResponseDto {
        private Long id;

        private Long bno;

        private String writer; // 작성자

        private String content;  //댓글 내용

        private LocalDateTime createDateTime;  //작성 시간

        public CommentResponseDto(Comment comment) {
            this.id = comment.getId();
            this.bno = comment.getBno();
            this.writer = comment.getWriter();
            this.content = comment.getContent();
            this.createDateTime = comment.getCreateDateTime();
        }
    }

}
