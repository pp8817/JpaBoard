package springJpaBoard.Board.api.apicontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.domain.argumenresolver.Login;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.LikeService;

@Slf4j
@RestController("/api/likes")
@RequiredArgsConstructor
public class LikeApiController {
    private final LikeService likeService;
    private final BoardService boardService;

    @GetMapping("/up/{boardId}")
    public ResponseEntity addLike(@PathVariable Long boardId, @Login Member loginMember) {

        Board board = boardService.findOne(boardId);
        likeService.addLike(loginMember, board);

        return ResponseEntity.status(HttpStatus.OK).body("게시글 좋아요 +1");
    }
}
