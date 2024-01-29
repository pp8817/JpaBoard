package springJpaBoard.Board.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.comment.api.CommentApiController;
import springJpaBoard.Board.domain.comment.exception.CommentNotFoundException;
import springJpaBoard.Board.domain.comment.model.Comment;
import springJpaBoard.Board.domain.comment.service.CommentService;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.service.MemberService;
import springJpaBoard.Board.global.Error.exception.ErrorCode;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springJpaBoard.Board.UtilsTemplate.*;
import static springJpaBoard.Board.comment.CommentTemplate.*;
import static springJpaBoard.Board.domain.comment.dto.CommentDto.CommentResponse;
import static springJpaBoard.Board.domain.comment.dto.CommentDto.CreateCommentRequest;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentApiController.class)
@DisplayName("CommentApiController 테스트")
public class CommentApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BoardService boardService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private MemberService memberService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Mock
    private Member member;
    @Mock
    private Board board;

    @Mock
    private Comment comment;

    @BeforeEach
    public void setUp() throws Exception {
        member = getMember();
        board = getBoard();
        comment = getComment();
    }

    @Test
    @DisplayName("[POST] 댓글 작성 - 로그인 세션 유효")
    public void 댓글_작성_로그인_세션_유효() throws Exception {
        //given
        final CommentResponse commentResponse = getCommentResponse();
        final CreateCommentRequest commentRequest = getCommentRequest();

        given(commentService.save(any(), any()))
                .willReturn(commentResponse);

        final MockHttpSession session = getSession(member);

        //when
        ResultActions actions = mockMvc.perform(post("/api/comments")
                .contentType(contentType)
                .session(session)
                .content(objectMapper.writeValueAsString(commentRequest)));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("댓글 작성 성공"))
                .andExpect(jsonPath("$.data.bno").value(commentResponse.bno()))
                .andExpect(jsonPath("$.data.writer").value(commentResponse.writer()))
                .andExpect(jsonPath("$.data.content").value(commentResponse.content()));
    }

    @Test
    @DisplayName("[POST] 댓글 작성 - 로그인 세션 유효 X")
    public void 댓글_작성_로그인_세션_유효_X() throws Exception {
        //given
        final MockHttpSession session = getSession(null);

        //when
        ResultActions actions = mockMvc.perform(post("/api/comments")
                .contentType(contentType)
                .session(session)
                .content(objectMapper.writeValueAsString(getCommentRequest())));

        //then
        actions
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("[DELETE] 댓글 삭제 - 로그인 세션 유효")
    public void 댓글_삭제_로그인_세션_유효() throws Exception {
        //given
        final Long commentId = 1L;

        given(commentService.findOne(commentId))
                .willReturn(comment);
        final MockHttpSession session = getSession(member);

        //when

        mockMvc.perform(delete("/api/comments/delete/{commentId}", 1L)
                        .contentType(contentType)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("댓글 삭제 성공"))
                .andExpect(jsonPath("$.data").value(commentId));

        //then
        verify(commentService, times(1)).delete(eq(commentId), eq(comment.getBno()));
    }

    @Test
    @DisplayName("[DELETE] 댓글 삭제 - 로그인 세션 유효 X")
    public void 댓글_삭제_로그인_세션_유효_X() throws Exception {
        //given
        final MockHttpSession session = getSession(null);

        //when
        ResultActions actions = mockMvc.perform(delete("/api/comments/delete/{commentId}", 1L)
                .contentType(contentType)
                .session(session));

        //then
        actions
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("[DELETE] 댓글 삭제 - 댓글이 존재하지 않는 경우")
    public void 댓글_삭제_댓글_정보_X() throws Exception {
        //given
        final Long commentId = 1L;
        doThrow(new CommentNotFoundException(commentId)).when(commentService).delete(commentId, comment.getBno());

        given(commentService.findOne(commentId))
                .willReturn(comment);

        final MockHttpSession session = getSession(member);

        //when

        ResultActions actions = mockMvc.perform(delete("/api/comments/delete/{commentId}", 1L)
                .contentType(contentType)
                .session(session));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.ENTITY_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.status").value(ErrorCode.ENTITY_NOT_FOUND.getStatus()))
                .andExpect(jsonPath("$.code").value(ErrorCode.ENTITY_NOT_FOUND.getCode()));
        verify(commentService, times(1)).delete(eq(commentId), eq(comment.getBno()));
    }
}
