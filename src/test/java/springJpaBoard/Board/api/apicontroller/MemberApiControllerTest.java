package springJpaBoard.Board.api.apicontroller;


import org.jetbrains.annotations.NotNull;
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
import springJpaBoard.Board.Error.exception.UserException;
import springJpaBoard.Board.SessionConst;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.StandardCharsets;

import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springJpaBoard.Board.controller.memberdto.MemberDto.MemberResponse;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberApiController.class)
@DisplayName("MemberApiController 테스트")
public class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BoardService boardService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Test
    @DisplayName("[GET] 회원 수정 - 로그인 세션이 유요한 경우")
    public void 회원수정_GET() throws Exception {
        // given
        long memberId = 1L;
        Member member = getMember();

        given(memberService.findOne(any()))
                .willReturn(member);

        given(memberService.loginValidation(member, member))
                .willReturn(TRUE);

        // 로그인 세션 생성
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        // when
        ResultActions actions = mockMvc.perform(get("/api/members/edit/{memberId}", memberId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 데이터 조회 성공"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("1"))
                .andExpect(jsonPath("$.data.gender").value("남성"));
    }

    @Test
    @DisplayName("[GET] 회원 수정 - 로그인 세션이 유효하지 않은 경우")
    public void 회원수정_GET_회원_정보_불일치() throws Exception {
        // given
        long memberId = 1L;
        Member member = getMember();
        MemberResponse updateMember = updateMember();

        given(memberService.findOne(any()))
                .willReturn(null);

        // 로그인 세션 생성
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        // when
        ResultActions actions = mockMvc.perform(get("/api/members/edit/{memberId}", memberId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("USER-EX"))
                .andExpect(jsonPath("$.message").value("회원 정보가 일치하지 않습니다."));
    }

    @Test
    @DisplayName("[PUT] 회원 수정")
    public void 회원수정_POST() throws Exception {
        // given
        long memberId = 1L;
        Member member = getMember();
        MemberResponse updateMember = updateMember();

        given(memberService.findOne(any()))
                .willReturn(member);

        given(memberService.loginValidation(member, member))
                .willReturn(TRUE);

        given(memberService.update(any(), any()))
                .willReturn(updateMember);

        // 로그인 세션 생성
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        // when
        ResultActions actions = mockMvc.perform(put("/api/members/edit/{memberId}", memberId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"id\": 1, \"name\": \"2\", \"gender\": \"여성\", " +
                        "\"city\": \"2\", \"street\": \"2\", \"zipcode\": \"2\" }"));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 정보 수정 성공"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("2"))
                .andExpect(jsonPath("$.data.gender").value("여성"));
    }

    @Test
    @DisplayName("[DELETE] 회원 삭제 - 성공")
    public void deleteMember_Success() throws Exception {
        // given
        long memberId = 1L;
        Member member = getMember();

        // 로그인 세션 생성
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        // when
        mockMvc.perform(delete("/api/members/delete/{memberId}", memberId)
                .session(session))
                .andExpect(status().isOk());

        // then
        verify(memberService, times(1)).delete(memberId);
    }

    @Test
    @DisplayName("[DELETE] 회원 삭제 - 회원이 존재하지 않음")
    public void deleteMember_MemberNotFound() throws Exception {
        // given
        long memberId = 1L;
        Member member = getMember();
        doThrow(new UserException("회원을 찾을 수 없습니다.")).when(memberService).delete(memberId);

        // 로그인 세션 생성
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        // when
        mockMvc.perform(delete("/api/members/delete/{memberId}", memberId)
                        .session(session))
                .andExpect(status().isBadRequest());

        // then
        verify(memberService, times(1)).delete(memberId);
    }

    @Test
    @DisplayName("[DELETE] 회원 삭제 - 로그인 되어 있지 않음")
    public void deleteMember_NotLoggedIn() throws Exception {
        // given
        long memberId = 1L;

        // when
        mockMvc.perform(delete("/api/members/delete/{memberId}", memberId))
                .andExpect(status().is3xxRedirection()); //로그인이 되어 있지 않다면 로그인 페이지로 Redirect
    }






    @NotNull
    private static Member getMember() {
        return Member.builder()
                .id(1L)
                .name("1")
                .gender("남성")
                .loginId("1")
                .password("1")
                .address(new Address("1", "1", "1"))
                .build();
    }

    @NotNull
    private static MemberResponse updateMember() {

        return MemberResponse.builder()
                .id(1L)
                .name("2")
                .gender("여성")
                .address(new Address("2", "2", "2"))
                .build();
    }

}