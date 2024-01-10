package springJpaBoard.Board.controller.commentdto;

import lombok.Builder;
import lombok.Getter;
import springJpaBoard.Board.domain.Comment;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentDto {
    /*Request*/
    @Getter
    @Builder
    public static class CreateCommentRequest {
        private Long bno;

        @NotBlank(message = "공백은 불가능합니다.")
        private String content;

        public CreateCommentRequest() {
        }
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
