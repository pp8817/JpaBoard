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
import springJpaBoard.Board.SesstionConst;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.StandardCharsets;

import static java.lang.Boolean.TRUE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @DisplayName("[GET] 회원 수정")
    public void 회원수정_GET() throws Exception {
        // given
        long memberId = 1L;
        Member member = getMember();
        Member updateMember = updateMember();

        given(memberService.findOne(any()))
                .willReturn(member);

        given(memberService.loginValidation(member, member))
                .willReturn(TRUE);

        // 로그인 세션 생성
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SesstionConst.LOGIN_MEMBER, member);

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
                .andExpect(jsonPath("$.data.gender").value("여성"));
    }

    @Test
    @DisplayName("[PUT] 회원 수정")
    public void 회원수정_POST() throws Exception {
        // given
        long memberId = 1L;
        Member member = getMember();
        Member updateMember = updateMember();

        given(memberService.findOne(any()))
                .willReturn(member);

        given(memberService.loginValidation(member, member))
                .willReturn(TRUE);

        given(memberService.update(any(), any()))
                .willReturn(updateMember);

        // 로그인 세션 생성
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SesstionConst.LOGIN_MEMBER, member);

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
                .andExpect(jsonPath("$.data.gender").value("여성"));
    }


    @NotNull
    private static Member getMember() {
        return Member.builder()
                .name("1")
                .gender("남성")
                .loginId("1")
                .password("1")
                .address(new Address("1", "1", "1"))
                .build();
    }

    @NotNull
    private static Member updateMember() {
        return Member.builder()
                .id(1L)
                .name("2")
                .gender("여성")
                .loginId("1")
                .password("1")
                .address(new Address("2", "2", "2"))
                .build();
    }

}