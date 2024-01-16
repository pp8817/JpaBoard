package springJpaBoard.Board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Comment;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.CommentRepository;

import java.util.NoSuchElementException;

import static springJpaBoard.Board.controller.commentdto.CommentDto.CommentResponse;
import static springJpaBoard.Board.controller.commentdto.CommentDto.CreateCommentRequest;

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

        commentRepository.save(comment); //쿼리 3

        return CommentResponse.of(comment);
    }

    public Comment findById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("댓글 정보가 없습니다."));

        if (comment == null) {
            throw new NoSuchElementException();
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
        Board board = boardService.findOne(bno);
        board.decreaseComment();
    }
}
