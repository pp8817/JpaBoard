package springJpaBoard.Board.like.controller;

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
import springJpaBoard.Board.api.apicontroller.LikeApiController;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.repository.LikeRepository;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.LikeService;

import java.nio.charset.StandardCharsets;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static springJpaBoard.Board.UtilsTemplate.getBoard;
import static springJpaBoard.Board.UtilsTemplate.getMember;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LikeApiController.class)
@DisplayName("LikeApiController 테스트")
public class LikeApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    LikeService likeService;
    
    @MockBean
    BoardService boardService;


    @MockBean
    LikeRepository likeRepository;

        protected MediaType contentType =
                new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    @DisplayName("[POST] 게시글 좋아요 - 로그인 세션 유효")
    public void 게시글_좋아요_로그인_세션_유효() throws Exception {
        //given

        Board board = getBoard();
        Member member = getMember();

        given(boardService.findOne(any()))
                .willReturn(board);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        //when
        mockMvc.perform(get("/api/likes/up/{boardId}", 1L)
                .contentType(contentType)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(content().string("게시글 좋아요 +1"));

        //then
        verify(likeService, times(1)).addLike(eq(member), eq(board));
    }

    @Test
    @DisplayName("[POST] 게시글 좋아요 - 로그인 세션 유효 X")
    public void 게시글_좋아요_로그인_세션_유효_X() throws Exception {
        //given

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, null);

        //when
        ResultActions actions = mockMvc.perform(get("/api/likes/up/{boardId}", 1L)
                .contentType(contentType)
                .session(session));

        //then
        actions
                .andExpect(status().is3xxRedirection());
    }
}
