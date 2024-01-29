package springJpaBoard.Board.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.comment.exception.CommentNotFoundException;
import springJpaBoard.Board.domain.comment.model.Comment;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.service.MemberService;
import springJpaBoard.Board.domain.comment.repository.CommentRepository;

import java.util.NoSuchElementException;

import static springJpaBoard.Board.domain.comment.dto.CommentDto.CommentResponse;
import static springJpaBoard.Board.domain.comment.dto.CommentDto.CreateCommentRequest;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final MemberService memberService;

    @Transactional
    public CommentResponse save(CreateCommentRequest commentRequestDTO, Long memberId) {

        Member member = memberService.findOne(memberId);
        Board board = boardService.findOne(commentRequestDTO.bno());

        Comment comment = Comment.builder()
                .bno(commentRequestDTO.bno())
                .writer(member.getName())
                .content(commentRequestDTO.content())
                .build();

        //연관 관계 편의 메서드 사용
        comment.setMember(member);
        comment.setBoard(board);
        board.addComment();

        commentRepository.save(comment);

        return CommentResponse.of(comment);
    }

    public Comment findOne(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
    }
    public Page<Comment> findAll(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    //게시글 번호에 해당하는 댓글 목록 조회 및 페이징 처리
    public Page<Comment> getCommentsByBno(Long bno, Pageable pageable) {
        return commentRepository.findAllByBno(bno, pageable);
    }

    @Transactional
    public void delete(Long commentId, Long bno) {
        if (commentRepository.findById(commentId).isPresent()) {
            commentRepository.deleteById(commentId);
            Board board = boardService.findOne(bno);
            board.decreaseComment();
        }
        throw new CommentNotFoundException(commentId);
    }
}
