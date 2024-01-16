package springJpaBoard.Board.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import springJpaBoard.Board.SessionConst;
import springJpaBoard.Board.api.apicontroller.CommentApiController;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springJpaBoard.Board.comment.CommentTemplate.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentApiController.class)
@DisplayName("CommentApiController 테스트")
public class CommentApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BoardService boardService;
    
    @MockBean
    private CommentService commentService;
    
    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    @DisplayName("[POST] 댓글 작성 - 로그인 세션 유효")
    public void 댓글_작성_로그인_세션_유효() throws Exception {
        //given
        given(commentService.save(any(), any()))
                .willReturn(getCommentResponse());

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, getMember());

        //when
        ResultActions actions = mockMvc.perform(post("/api/comments")
                .contentType(contentType)
                .session(session)
                .content(objectMapper.writeValueAsString(getCommentRequest())));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("댓글 작성 성공"))
                .andExpect(jsonPath("$.data.bno").value(1L))
                .andExpect(jsonPath("$.data.writer").value("writer"))
                .andExpect(jsonPath("$.data.content").value("content"));
    }

    @Test
    @DisplayName("[POST] 댓글 작성 - 로그인 세션 유효 X")
    public void 댓글_작성_로그인_세션_유효_X() throws Exception {
        //given
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, null);

        //when
        ResultActions actions = mockMvc.perform(post("/api/comments")
                .contentType(contentType)
                .session(session)
                .content(objectMapper.writeValueAsString(getCommentRequest())));

        //then
        actions
                .andExpect(status().is3xxRedirection());
    }

//    @Test
//    @DisplayName("[DELETE] 댓글 삭제 - 로그인 세션 유효")
//    public void 댓글_삭제_로그인_세션_유효() throws Exception {
//        //given
//        given(commentService.findOne(any()))
//                .willReturn(getComment());
//        MockHttpSession session = new MockHttpSession();
//        session.setAttribute(SessionConst.LOGIN_MEMBER, getMember());
//
//        //when
//
//        ResultActions actions = mockMvc.perform(delete("/api/comments/{commentId}", 1L)
//                .contentType(contentType)
//                .session(session));
//
//        //then
//        actions
//                .andExpect(status().isOk());
//    }
}
