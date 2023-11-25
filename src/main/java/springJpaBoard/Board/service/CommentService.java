package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;

    @Transactional
    public void save(Comment comment, Long memberId) {
        //엔티티 조회
        Member member = memberService.findOne(memberId);
        //연관 관계 생성
        comment.setMember(member);

        commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).get();
    }

    //게시글 번호에 해당하는 댓글 목록 조회 및 페이징 처리
    public Page<Comment> getCommentsByBno(Long bno, Pageable pageable) {
        return commentRepository.findAllByBno(bno, pageable);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
