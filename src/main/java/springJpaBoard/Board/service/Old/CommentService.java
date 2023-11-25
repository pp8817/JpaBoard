package springJpaBoard.Board.service.Old;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.CommentRepositoryImpl;
import springJpaBoard.Board.service.MemberService;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepositoryImpl commentRepository;
    private final MemberService memberService;

    @Transactional
    public void save(Comment comment, Long memberId) {
        //엔티티 조회
        Member member = memberService.findOne(memberId);
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
