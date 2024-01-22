package springJpaBoard.Board.like.controller;

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
import springJpaBoard.Board.domain.like.controller.LikeController;
import springJpaBoard.Board.domain.like.repository.LikeRepository;
import springJpaBoard.Board.domain.like.service.LikeService;
import springJpaBoard.Board.domain.member.model.Member;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static springJpaBoard.Board.UtilsTemplate.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LikeController.class)
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

    @Mock
    Member member;
    @Mock
    Board board;

    @BeforeEach
    public void setUp() throws Exception {
        member = getMember();
        board = getBoard();
    }

//    @Test
//    @DisplayName("[POST] 게시글 좋아요 - 로그인 세션 유효")
//    public void 게시글_좋아요_로그인_세션_유효() throws Exception {
//        //given
//
//        given(boardService.findOne(any()))
//                .willReturn(board);
//
//        final MockHttpSession session = getSession(member);
//
//        //when
//        mockMvc.perform(get("/api/likes/up/{boardId}", 1L)
//                        .contentType(contentType)
//                        .session(session))
//                .andExpect(status().isOk())
//                .andExpect(content().string("게시글 좋아요 +1"));
//
//        //then
//        verify(likeService, times(1)).addLike(eq(member), eq(board));
//    }

    @Test
    @DisplayName("[POST] 게시글 좋아요 - 로그인 세션 유효 X")
    public void 게시글_좋아요_로그인_세션_유효_X() throws Exception {
        //given
        final MockHttpSession session = getSession(null);

        //when
        ResultActions actions = mockMvc.perform(get("/api/likes/up/{boardId}", 1L)
                .contentType(contentType)
                .session(session));

        //then
        actions
                .andExpect(status().is3xxRedirection());
    }
}
