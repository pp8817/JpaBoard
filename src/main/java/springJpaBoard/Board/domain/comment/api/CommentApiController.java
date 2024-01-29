package springJpaBoard.Board.domain.comment.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.comment.model.Comment;
import springJpaBoard.Board.domain.comment.service.CommentService;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.service.MemberService;
import springJpaBoard.Board.global.Error.Message;
import springJpaBoard.Board.global.Error.StatusEnum;
import springJpaBoard.Board.global.argumenresolver.Login;

import java.nio.charset.Charset;

import static springJpaBoard.Board.domain.comment.dto.CommentDto.CommentResponse;
import static springJpaBoard.Board.domain.comment.dto.CommentDto.CreateCommentRequest;

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
    public ResponseEntity saveComment(@RequestBody final CreateCommentRequest commentRequestDTO, BindingResult result, @Login final Member loginMember) {

        final CommentResponse commentDto = commentService.save(commentRequestDTO, loginMember.getId());

        final Message message = new Message(StatusEnum.OK, "댓글 작성 성공", commentDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity deleteComment(@PathVariable final Long commentId) {

        final Comment comment = commentService.findOne(commentId); // 해당 댓글이 존재하지 않는 경우 여기서 Exception 발생
        commentService.delete(commentId, comment.getBno());

        final Message message = new Message(StatusEnum.OK, "댓글 삭제 성공", commentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }
}
