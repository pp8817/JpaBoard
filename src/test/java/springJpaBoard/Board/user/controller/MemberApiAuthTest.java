package springJpaBoard.Board.user.controller;

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
import springJpaBoard.Board.api.apicontroller.MemberApiController;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springJpaBoard.Board.UtilsTemplate.getMember;
import static springJpaBoard.Board.UtilsTemplate.getSession;
import static springJpaBoard.Board.controller.memberdto.AuthDto.LoginRequest;
import static springJpaBoard.Board.controller.memberdto.MemberDto.CreateMemberRequest;
import static springJpaBoard.Board.user.UserTemplate.getCreateMemberRequest;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberApiController.class)
@DisplayName("회원가입, 로그인 테스트")
public class MemberApiAuthTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BoardService boardService;

    @Mock
    private Member member;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @BeforeEach
    public void setUp() throws Exception {
        member = getMember();
    }


    @Test
    @DisplayName("[POST] 회원 가입")
    public void 회원가입() throws Exception {
        //given
        given(memberService.join(any()))
                .willReturn(1L);

        final CreateMemberRequest createMemberRequest = getCreateMemberRequest();

        //when
        ResultActions actions = mockMvc.perform(post("/api/members")
                .contentType(contentType)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createMemberRequest))
        );
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("회원 가입 성공"))
                .andExpect(jsonPath("$.data").value("1"));

    }

    @Test
    @DisplayName("[POST] 로그인")
    public void 로그인() throws Exception {
        //given
        given(memberService.login("1", "1"))
                .willReturn(member);

        final LoginRequest loginRequest = LoginRequest.builder()
                .loginId("1")
                .password("1")
                .build();
        //when
        ResultActions actions = mockMvc.perform(post("/api/members/login")
                .contentType(contentType)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(loginRequest))
        );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data").value("1"));
    }

    @Test
    @DisplayName("[POST] 로그인 실패 - 검증")
    public void 로그인_실패_검증() throws Exception {
        //given
        given(memberService.login("1", "1"))
                .willReturn(member);

        //when
        ResultActions actions = mockMvc.perform(post("/api/members/login")
                .contentType(contentType)
                .characterEncoding("UTF-8")
                .content("{" +
                        "    \"password\" : \"1\"" +
                        "}")
        );
        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.code").value("UserException"))
                .andExpect(jsonPath("$.message").value("로그인: 아이디 또는 비밀번호 오류"));
    }

    @Test
    @DisplayName("[POST] 로그아웃 - 세션이 있는 경우")
    void logoutWithSession() throws Exception {
        //given
        MockHttpSession session = getSession(member);

        // when
        ResultActions actions = mockMvc.perform(post("/api/members/logout")
                .session(session)
                .contentType(contentType));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 로그아웃 성공"));
    }

    @Test
    @DisplayName("[POST] 로그아웃 - 세션이 없는 경우")
    void logoutWithoutSession() throws Exception {
        //given
        MockHttpSession session = getSession(null);

        // when
        ResultActions actions = mockMvc.perform(post("/api/members/logout")
                .contentType(contentType));

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("세션이 존재하지 않습니다."));
    }
}
