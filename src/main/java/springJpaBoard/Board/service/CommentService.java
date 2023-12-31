package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.BoardRepository;
import springJpaBoard.Board.repository.CommentRepository;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberService memberService;

    @Transactional
    public void save(Comment comment, Long memberId) {
        //엔티티 조회
        Member member = memberService.findOne(memberId);

        //연관 관계 편의 메서드 사용
        comment.setMember(member);

        commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).get();
    }

    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    //게시글 번호에 해당하는 댓글 목록 조회 및 페이징 처리
    public Page<Comment> getCommentsByBno(Long bno, Pageable pageable) {
        return commentRepository.findAllByBno(bno, pageable);
    }

    @Transactional
    public void delete(Long id, Long bno) {
        commentRepository.deleteById(id);
        Board board = boardRepository.findById(bno).get();
        board.decreaseComment();
    }
}
