package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.CommentRepositoryImpl;
import springJpaBoard.Board.repository.MemberRepositoryImpl;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardService boardService;
    private final CommentRepositoryImpl commentRepository;
    private final MemberRepositoryImpl memberRepository;

    @Transactional
    public void save(Comment comment, Long memberId) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        //연관 관계 생성
        comment.setMember(member);

        commentRepository.insert(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.delete(id);
    }
}
