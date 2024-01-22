package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Like;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.LikeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class LikeService {
    private final LikeRepository likeRepository;

    public void addLike(Member member, Board board) {

        if (!likeRepository.existsByMemberAndBoard(member, board)) {
            //호출되면 board에 있는 recommend 증가
            board.addLike();

            Like like = new Like(member, board);
            likeRepository.save(like);
        } else {
            board.decreaseLike();
            likeRepository.deleteByMemberAndBoard(member, board);
        }
    }
}
