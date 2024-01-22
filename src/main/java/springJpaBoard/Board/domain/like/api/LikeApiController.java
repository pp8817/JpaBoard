package springJpaBoard.Board.domain.like.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.like.service.LikeService;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.global.argumenresolver.Login;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeApiController {
    private final LikeService likeService;
    private final BoardService boardService;

    @GetMapping(value = "/up/{boardId}")
    public ResponseEntity addLike(@PathVariable final Long boardId, @Login final Member loginMember) {

        final Board board = boardService.findOne(boardId);
        likeService.addLike(loginMember, board);

        return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요 +1");
    }
}