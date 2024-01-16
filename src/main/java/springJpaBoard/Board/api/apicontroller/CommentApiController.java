package springJpaBoard.Board.api.apicontroller;

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
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.Charset;

import static springJpaBoard.Board.controller.commentdto.CommentDto.CommentResponse;
import static springJpaBoard.Board.controller.commentdto.CommentDto.CreateCommentRequest;

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
    public ResponseEntity saveComment(@RequestBody CreateCommentRequest commentRequestDTO, BindingResult result, @Login Member loginMember) {

        if (result.hasErrors()) {
            throw new IllegalStateException("양식 불일치 오류");
        }

        CommentResponse commentDto = commentService.save(commentRequestDTO, loginMember.getId());

        Message message = new Message(StatusEnum.OK, "댓글 작성 성공", commentDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId) {

        Comment comment = commentService.findOne(commentId); // 해당 댓글이 존재하지 않는 경우 여기서 Exception 발생
        commentService.delete(commentId, comment.getBno());

        Message message = new Message(StatusEnum.OK, "댓글 삭제 성공", commentId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(message, headers, HttpStatus.OK);
    }
}
