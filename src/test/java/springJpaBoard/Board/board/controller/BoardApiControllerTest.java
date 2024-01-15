package springJpaBoard.Board.board.controller;

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
import springJpaBoard.Board.api.apicontroller.BoardApiController;
import springJpaBoard.Board.api.apirepository.BoardApiRepository;
import springJpaBoard.Board.domain.Board;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.StandardCharsets;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springJpaBoard.Board.board.BoardTemplate.*;
import static springJpaBoard.Board.controller.boarddto.BoardDto.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BoardApiController.class)
@DisplayName("BoardApiController 테스트")
class BoardApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BoardApiRepository boardApiRepository;

    @Autowired
    private ObjectMapper objectMapper;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    @DisplayName("[GET] 게시글 작성")
    public void 게시글_작성_GET() throws Exception {
        //given
        Member member = getMember();

        /*로그인 세션*/
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(1L));
    }

    @Test
    @DisplayName("[POST] 게시글 작성 - 로그인 세션이 유효한 경우")
    public void 게시글_작성_POST() throws Exception {
        //given
        Member member = getMember();
        CreateBoardRequest request = CreateBoardRequest.builder()
                .title("title")
                .writer("username")
                .content("content")
                .build();

        /*로그인 세션*/
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        given(boardService.write(any(), any()))
                .willReturn(1L);

        //when
        ResultActions actions = mockMvc.perform(post("/api/boards")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 작성 성공"))
                .andExpect(jsonPath("$.data").value(1));
    }

    @Test
    @DisplayName("[POST] 게시글 작성 - 로그인 세션이 유효하지 않은 경우")
    public void 게시글_작성_로그인_세션_X() throws Exception {
        //given
        Member member = getMember();
        CreateBoardRequest request = CreateBoardRequest.builder()
                .title("title")
                .writer("username")
                .content("content")
                .build();

        given(boardService.write(any(), any()))
                .willReturn(1L);

        /*로그인 세션*/
        // 로그인 세션 생성
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, null);

        //when
        ResultActions actions = mockMvc.perform(post("/api/boards")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        actions
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("[POST] 게시글 작성 - 양식 오류")
    public void 게시글_작성_검증_오류() throws Exception {
        //given
        Member member = getMember();
        CreateBoardRequest request = CreateBoardRequest.builder()
                .title("")
                .writer("username")
                .content("content")
                .build();

        /*로그인 세션*/
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        given(boardService.write(any(), any()))
                .willReturn(1L);

        //when
        ResultActions actions = mockMvc.perform(post("/api/boards")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        //then
        actions
                .andExpect(status().is5xxServerError());
    }

    @Test
    @DisplayName("[GET] 게시글 상세")
    public void 게시글_상세() throws Exception {
        //given
        Long boardId = 1L;
        Board board = getBoard();

        given(boardService.findOne(any()))
                .willReturn(board);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards/detail/{boardId}", boardId)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 상세 페이지 조회 성공"))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value("content"))
                .andExpect(jsonPath("$.data.writer").value("writer"))
                .andExpect(jsonPath("$.data.likes").value(0));
    }

    @Test
    @DisplayName("[GET] 게시글 수정 - 로그인 세션 유효")
    public void 게시글_수정_페이지_로그인_세션_유효() throws Exception {
        //given
        Long boardId = 1L;
        Member member = getMember();
        Board board = getBoard();
        board.setMember(member);

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        loginValidation(member, TRUE);

        /*로그인 세션*/
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards/edit/{boardId}", boardId)
                        .session(session)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 수정 페이지 조회"))
                .andExpect(jsonPath("$.data.title").value("title"))
                .andExpect(jsonPath("$.data.content").value("content"))
                .andExpect(jsonPath("$.data.writer").value("writer"));
    }

    @Test
    @DisplayName("[GET] 게시글 수정 - 로그인 세션 유효하지 않음")
    public void 게시글_수정_페이지_로그인_세션_유효_X() throws Exception {
        //given
        Long boardId = 1L;
        Member member = getMember();
        Board board = getBoard();
        board.setMember(member);


        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        loginValidation(member, TRUE);

        /*로그인 세션*/
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, null);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards/edit/{boardId}", boardId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("[GET] 게시글 수정 - 회원 정보 불일치")
    public void 게시글_수정_페이지_회원_정보_불일치() throws Exception {
        //given
        Long boardId = 1L;
        Member member = getMember();
        Board board = getBoard();
        board.setMember(member);

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        loginValidation(member, FALSE);

        /*로그인 세션*/
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        //when
        ResultActions actions = mockMvc.perform(get("/api/boards/edit/{boardId}", boardId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("게시글 회원 정보와 로그인 회원 정보가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("[PUT] 게시글 수정 - 로그인 세션 유효")
    public void 게시글_수정_로그인_세션_유효() throws Exception {
        //given
        Member member = getMember();
        Board board = getBoard();
        board.setMember(member);
        ModifyBoardRequest modifyBoardRequest = getModifyBoardRequest();
        ModifyBoardResponse modifyBoardResponse = getModifyBoardResponse();

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        given(boardService.update(any(), any()))
                .willReturn(modifyBoardResponse);

        loginValidation(member, TRUE);

        /*로그인 세션*/
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        //when
        ResultActions actions = mockMvc.perform(put("/api/boards/edit/{boardId}", 1L)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyBoardRequest)));

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("게시글 수정 성공"))
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.title").value("2"))
                .andExpect(jsonPath("$.data.writer").value("writer"))
                .andExpect(jsonPath("$.data.content").value("2"));

    }

    @Test
    @DisplayName("[PUT] 게시글 수정 - 회원 정보 불일치")
    public void 게시글_수정_회원_정보_불일치() throws Exception {
        //given
        Member member = getMember();
        Board board = getBoard();
        board.setMember(member);
        ModifyBoardRequest modifyBoardRequest = getModifyBoardRequest();

        given(boardApiRepository.findBoardWithMember(any()))
                .willReturn(board);

        loginValidation(member, FALSE);

        /*로그인 세션*/
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        //when
        ResultActions actions = mockMvc.perform(put("/api/boards/edit/{boardId}", 1L)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(modifyBoardRequest)));

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("게시글 회원 정보와 로그인 회원 정보 불일치"));
    }

    private void loginValidation(Member member, Boolean bool) {
        if (bool) {
            given(memberService.loginValidation(member, member))
                    .willReturn(TRUE);
        }
        if (!bool) {
            given(memberService.loginValidation(member, member))
                    .willReturn(FALSE);
        }
    }


}