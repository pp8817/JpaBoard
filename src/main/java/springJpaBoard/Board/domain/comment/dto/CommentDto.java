package springJpaBoard.Board.domain.comment.dto;

import lombok.Builder;
import springJpaBoard.Board.domain.comment.model.Comment;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentDto {
    /*Request*/
    @Builder
    public record CreateCommentRequest(
            Long bno,
            @NotBlank(message = "공백은 불가능합니다.")
            String content
    ) {
    }

    /*Response*/
    @Builder
    public record CommentResponse(
            Long id,
            Long bno,
            String writer,
            String content,
            LocalDateTime createDateTime
    ) {
        public static CommentResponse of(Comment comment) {
            return CommentResponse.builder()
                    .id(comment.getId())
                    .bno(comment.getBno())
                    .writer(comment.getWriter())
                    .content(comment.getContent())
                    .createDateTime(LocalDateTime.now())
                    .build();
        }
    }
}
