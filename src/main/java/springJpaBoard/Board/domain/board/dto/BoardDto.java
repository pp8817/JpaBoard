package springJpaBoard.Board.domain.board.dto;

import lombok.Builder;
import springJpaBoard.Board.domain.board.model.Board;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

import static springJpaBoard.Board.domain.comment.dto.CommentDto.CommentResponse;

public class BoardDto {

    /*Request*/
    @Builder
    public record CreateBoardRequest(
            Long id,
            @NotBlank(message = "제목은 필수입니다.")
            @Size(min = 1, max = 50, message = "제목의 길이는 1~50자 사이입니다.")
            String title,
            @NotBlank(message = "작성자는 필수입니다.")
            @Size(min = 1, max = 30, message = "1~30자 사이만 가능합니다.")
            String writer,
            @Size(max = 300, message = "300자가 최대입니다.")
            String content,
            LocalDateTime modifyDateTime
    ) {
        public Board toEntity() {
            return Board.builder()
                    .title(title)
                    .writer(writer)
                    .content(content)
                    .localDateTime(LocalDateTime.now())
                    .build();
        }
    }

    @Builder
    public record ModifyBoardRequest(
            @NotBlank(message = "제목은 필수입니다.")
            @Size(min = 1, max = 50, message = "제목의 길이는 1~50자 사이입니다.")
            String title,
            @Size(max = 300, message = "300자가 최대입니다.")
            String content
    ) {

    }


    /*Response*/
    @Builder
    public record BoardResponse(
            Long id,
            String title,
            String content,
            String writer,
            int likes,
            LocalDateTime boardDateTime,
            List<CommentResponse> comments
    ) {
        public static BoardResponse of(Board board, List<CommentResponse> comments) {
            return BoardResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .writer(board.getWriter())
                    .content(board.getContent())
                    .likes(board.getLikes())
                    .boardDateTime(board.getBoardDateTime())
                    .comments(comments)
                    .build();
        }
    }

    @Builder
    public record BoardDetailResponse(
            Long id,
            String title,
            String writer,
            int view,
            int commentCount,
            LocalDateTime boardDateTime
    ) {
        public static BoardDetailResponse of(Board board) {
            return BoardDetailResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .writer(board.getWriter())
                    .view(board.getView())
                    .commentCount(board.getCommentCount())
                    .boardDateTime(board.getBoardDateTime())
                    .build();
        }
    }

    @Builder
    public record ModifyBoardResponse(
            Long id,
            String title,
            String writer,
            String content
    ) {
        public static ModifyBoardResponse of(Board board) {
            return ModifyBoardResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .writer(board.getWriter())
                    .content(board.getContent())
                    .build();
        }
    }


    @Builder
    public record BoardListResponse(
            Long id,
            String name,
            String title,
            String writer,
            int view,
            LocalDateTime localDateTime,
            int commentCount
    ) {
        public static BoardListResponse of(Board board) {
            return BoardListResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .name(board.getMember().getName())
                    .writer(board.getWriter())
                    .view(board.getView())
                    .localDateTime(board.getBoardDateTime())
                    .commentCount(board.getCommentCount())
                    .build();
        }
    }

    @Builder
    public record MyPostsResponse(
            Long id,
            String title,
            String writer,
            int view,
            LocalDateTime localDateTime,
            int commentCount
    ) {
        public static MyPostsResponse of(Board board) {
            return MyPostsResponse.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .writer(board.getWriter())
                    .view(board.getView())
                    .localDateTime(board.getBoardDateTime())
                    .commentCount(board.getCommentCount())
                    .build();
        }
    }

}
