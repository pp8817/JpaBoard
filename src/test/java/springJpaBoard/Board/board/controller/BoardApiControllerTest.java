package springJpaBoard.Board.board.controller;

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
import springJpaBoard.Board.domain.board.api.BoardApiController;
import springJpaBoard.Board.domain.board.model.Board;
import springJpaBoard.Board.domain.board.repository.BoardApiRepository;
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.comment.service.CommentService;
import springJpaBoard.Board.domain.member.exception.UserException;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.service.MemberService;
import springJpaBoard.Board.global.Error.exception.ErrorCode;

import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springJpaBoard.Board.UtilsTemplate.*;
import static springJpaBoard.Board.board.BoardTemplate.*;
import static springJpaBoard.Board.domain.board.dto.BoardDto.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BoardApiController.class)
@DisplayName("BoardApiController 테스트")
class BoardApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BoardApiRepository boardApiRepository;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Mock
    private Member member;
    @Mock
    private Board board;

    @BeforeEach
    public void setUp() throws Exception {
        member = getMember();
        board = getBoard();
    }

    @Test
    @DisplayName("[GET] 게시글 작성")
    public void 게시글_작성_GET() throws Exception {
        //given
        final MockHttpSession session = getSession(member);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards")
                .session(session)
                .contentType(contentType));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    @DisplayName("[POST] 게시글 작성 - 로그인 세션이 유효한 경우")
    public void 게시글_작성_POST() throws Exception {
        //given
        final CreateBoardRequest createBoardRequest = getCreateBoardRequest();

        /*로그인 세션*/
        final MockHttpSession session = getSession(member);

        given(boardService.write(any(), any()))
                .willReturn(1L);

        //when
        ResultActions actions = mockMvc.perform(post("/api/boards")
                .session(session)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(createBoardRequest)));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 작성 성공"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    @DisplayName("[POST] 게시글 작성 - 로그인 세션이 유효하지 않은 경우")
    public void 게시글_작성_로그인_세션_X() throws Exception {
        //given
        final CreateBoardRequest createBoardRequest = getCreateBoardRequest();

        given(boardService.write(any(), any()))
                .willReturn(1L);

        /*로그인 세션*/
        // 로그인 세션 생성
        final MockHttpSession session = getSession(null);

        //when
        ResultActions actions = mockMvc.perform(post("/api/boards")
                .session(session)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(createBoardRequest)));
        //then
        actions
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("[POST] 게시글 작성 - 양식 오류")
    public void 게시글_작성_검증_오류() throws Exception {
        //given
        final CreateBoardRequest request = CreateBoardRequest.builder()
                .title("")
                .writer("username")
                .content("content")
                .build();

        /*로그인 세션*/
        final MockHttpSession session = getSession(member);

        given(boardService.write(any(), any()))
                .willReturn(1L);

        //when
        ResultActions actions = mockMvc.perform(post("/api/boards")
                .session(session)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(request)));
        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C001"))
                .andExpect(jsonPath("$.message").value("잘못된 입력값입니다."));
    }

    @Test
    @DisplayName("[GET] 게시글 상세")
    public void 게시글_상세() throws Exception {
        //given
        final Long boardId = 1L;

        given(boardService.findOne(any()))
                .willReturn(board);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards/detail/{boardId}", boardId)
                .contentType(contentType));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 상세 페이지 조회 성공"))
                .andExpect(jsonPath("$.data.title").value(board.getTitle()))
                .andExpect(jsonPath("$.data.content").value(board.getContent()))
                .andExpect(jsonPath("$.data.writer").value(board.getWriter()))
                .andExpect(jsonPath("$.data.likes").value(0));
    }

    @Test
    @DisplayName("[GET] 게시글 수정 - 로그인 세션 유효")
    public void 게시글_수정_페이지_로그인_세션_유효() throws Exception {
        //given
        final Long boardId = 1L;
        board.setMember(member);

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        /*로그인 세션*/
        final MockHttpSession session = getSession(member);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards/edit/{boardId}", boardId)
                .session(session)
                .contentType(contentType));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 수정 페이지 조회"))
                .andExpect(jsonPath("$.data.title").value(board.getTitle()))
                .andExpect(jsonPath("$.data.content").value(board.getContent()))
                .andExpect(jsonPath("$.data.writer").value(board.getWriter()));
    }

    @Test
    @DisplayName("[GET] 게시글 수정 - 로그인 세션 유효하지 않음")
    public void 게시글_수정_페이지_로그인_세션_유효_X() throws Exception {
        //given
        final Long boardId = 1L;
        board.setMember(member);

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        /*로그인 세션*/
        MockHttpSession session = getSession(null);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards/edit/{boardId}", boardId)
                .session(session)
                .contentType(contentType));

        //then
        actions
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("[GET] 게시글 수정 - 회원 정보 불일치")
    public void 게시글_수정_페이지_회원_정보_불일치() throws Exception {
        //given
        final Long boardId = 1L;
        board.setMember(member);
        doThrow(new UserException(ErrorCode.USER_MISMATCH)).when(memberService).loginValidation(any(), any());

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        /*로그인 세션*/
        final MockHttpSession session = getSession(member);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards/edit/{boardId}", boardId)
                .session(session)
                .contentType(contentType));

        //then
        actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("회원 정보가 불일치합니다."))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("A004"));
        verify(memberService, times(1)).loginValidation(any(), any());
    }

    @Test
    @DisplayName("[PUT] 게시글 수정 - 로그인 세션 유효")
    public void 게시글_수정_로그인_세션_유효() throws Exception {
        //given
        board.setMember(member);
        final ModifyBoardRequest modifyBoardRequest = getModifyBoardRequest();
        final ModifyBoardResponse modifyBoardResponse = getModifyBoardResponse();

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        given(boardService.update(any(), any()))
                .willReturn(modifyBoardResponse);

        /*로그인 세션*/
        final MockHttpSession session = getSession(member);

        //when
        ResultActions actions = mockMvc.perform(put("/api/boards/edit/{boardId}", 1L)
                .session(session)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(modifyBoardRequest)));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 수정 성공"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value(modifyBoardResponse.title()))
                .andExpect(jsonPath("$.data.writer").value(modifyBoardResponse.writer()))
                .andExpect(jsonPath("$.data.content").value(modifyBoardResponse.content()));
    }

    @Test
    @DisplayName("[PUT] 게시글 수정 - 회원 정보 불일치")
    public void 게시글_수정_회원_정보_불일치() throws Exception {
        //given
        board.setMember(member);
        final ModifyBoardRequest modifyBoardRequest = getModifyBoardRequest();

        doThrow(new UserException(ErrorCode.USER_MISMATCH)).when(memberService).loginValidation(any(), any());

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        /*로그인 세션*/
        final MockHttpSession session = getSession(member);

        //when
        ResultActions actions = mockMvc.perform(put("/api/boards/edit/{boardId}", 1L)
                .session(session)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(modifyBoardRequest)));

        //then
        actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("회원 정보가 불일치합니다."))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("A004"));
        verify(memberService, times(1)).loginValidation(any(), any());
    }

    @Test
    @DisplayName("[DELETE] 게시글 삭제 - 로그인 세션 유효")
    public void 게시글_삭제_로그인_세션_유효() throws Exception {
        // given
        final Long boardId = 1L;

        final MockHttpSession session = getSession(member);

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        // when
        mockMvc.perform(delete("/api/boards/delete/{boardId}", boardId)
                        .session(session)
                        .contentType(contentType))
                .andExpect(status().isOk());

        // then
        verify(boardService, times(1)).delete(boardId);
    }

    @Test
    @DisplayName("[DELETE] 게시글 삭제 - 로그인 세션 유효_X")
    public void 게시글_삭제_로그인_세션_유효_X() throws Exception {
        // given
        final Long boardId = 1L;

        final MockHttpSession session = getSession(null);

        // when
        ResultActions actions = mockMvc.perform(delete("/api/boards/delete/{boardId}", boardId)
                .session(session)
                .contentType(contentType));

        // then
        actions
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("[DELETE] 게시글 삭제 - 게시글이 존재하지 않음")
    public void 게시글_삭제_게시글_존재_X() throws Exception {
        // given
        Long boardId = 1L;

        doThrow(new NoSuchElementException("게시글을 찾을 수 없습니다.")).when(boardService).delete(boardId);

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        final MockHttpSession session = getSession(member);


        // when
        mockMvc.perform(delete("/api/boards/delete/{boardId}", boardId)
                        .session(session)
                        .contentType(contentType))
                .andExpect(status().is5xxServerError());

        // then
        verify(boardService, times(1)).delete(boardId);
    }
}