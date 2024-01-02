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
import springJpaBoard.Board.repository.MemberRepository;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(Comment comment, Member member, Board board) {
        //연관 관계 편의 메서드 사용
        comment.setMember(member);
        comment.setBoard(board);
        board.addComment();

        commentRepository.save(comment); //쿼리 3
    }

    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id).get();

        if (comment == null) {
            throw new NoSuchElementException("해당 ID에 대한 댓글을 찾을 수 없습니다.");
        }

        return comment;
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
