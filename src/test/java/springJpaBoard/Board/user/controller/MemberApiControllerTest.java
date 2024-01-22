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
import springJpaBoard.Board.domain.board.service.BoardService;
import springJpaBoard.Board.domain.member.api.MemberApiController;
import springJpaBoard.Board.domain.member.exception.MemberNotFoundException;
import springJpaBoard.Board.domain.member.exception.UserException;
import springJpaBoard.Board.domain.member.model.Member;
import springJpaBoard.Board.domain.member.service.MemberService;
import springJpaBoard.Board.global.Error.exception.ErrorCode;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static springJpaBoard.Board.UtilsTemplate.getMember;
import static springJpaBoard.Board.UtilsTemplate.getSession;
import static springJpaBoard.Board.domain.member.dto.MemberDto.MemberResponse;
import static springJpaBoard.Board.domain.member.dto.MemberDto.ModifyMemberRequest;
import static springJpaBoard.Board.user.UserTemplate.getModifyMemberRequest;
import static springJpaBoard.Board.user.UserTemplate.updateMember;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberApiController.class)
@DisplayName("MemberApiController 테스트")
public class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BoardService boardService;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    @Mock
    private Member member;

    @BeforeEach
    public void setUp() throws Exception {
        member = getMember();
    }

    @Test
    @DisplayName("[GET] 회원 수정 - 로그인 세션이 유요한 경우")
    public void 회원수정_GET() throws Exception {
        // given
        final Long memberId = member.getId();

        given(memberService.findOne(any()))
                .willReturn(member);

        final MockHttpSession session = getSession(member);

        // when
        ResultActions actions = mockMvc.perform(get("/api/members/edit/{memberId}", memberId)
                .session(session)
                .contentType(contentType));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 데이터 조회 성공"))
                .andExpect(jsonPath("$.data.id").value(memberId))
                .andExpect(jsonPath("$.data.name").value(member.getName()))
                .andExpect(jsonPath("$.data.gender").value(member.getGender()));
    }

    @Test
    @DisplayName("[GET] 회원 수정 - 로그인 세션이 유효하지 않은 경우")
    public void 회원수정_GET_회원_정보_불일치() throws Exception {
        // given
        final Long memberId = member.getId();

        given(memberService.findOne(any()))
                .willReturn(member);

        // 로그인 세션 생성
        final MockHttpSession session = getSession(null);

        // when
        ResultActions actions = mockMvc.perform(get("/api/members/edit/{memberId}", memberId)
                .session(session)
                .contentType(contentType));

        // then
        actions
                .andExpect(status().is3xxRedirection());
    }


    @Test
    @DisplayName("[PUT] 회원 수정")
    public void 회원수정_POST() throws Exception {
        // given
        final MemberResponse updateMember = updateMember();

        final Long memberId = member.getId();

        given(memberService.findOne(any()))
                .willReturn(member);

        given(memberService.update(any(), any()))
                .willReturn(updateMember);

        final ModifyMemberRequest modifyMemberRequest = getModifyMemberRequest();

        // 로그인 세션 생성
        final MockHttpSession session = getSession(member);

        // when
        ResultActions actions = mockMvc.perform(put("/api/members/edit/{memberId}", memberId)
                .session(session)
                .contentType(contentType)
                .content(objectMapper.writeValueAsString(modifyMemberRequest)));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(contentType))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("회원 정보 수정 성공"))
                .andExpect(jsonPath("$.data.id").value(modifyMemberRequest.id()))
                .andExpect(jsonPath("$.data.name").value(modifyMemberRequest.name()))
                .andExpect(jsonPath("$.data.gender").value(modifyMemberRequest.gender()))
                .andExpect(jsonPath("$.data.address.city").value(modifyMemberRequest.address().getCity()))
                .andExpect(jsonPath("$.data.address.street").value(modifyMemberRequest.address().getStreet()))
                .andExpect(jsonPath("$.data.address.zipcode").value(modifyMemberRequest.address().getZipcode()));
    }

    @Test
    @DisplayName("[DELETE] 회원 삭제 - 로그인 세션 유효")
    public void 회원_삭제_로그인_세션_유효() throws Exception {
        // given
        final Long memberId = member.getId();

        given(memberService.findOne(any()))
                .willReturn(member);

        // 로그인 세션 생성
        final MockHttpSession session = getSession(member);

        // when
        mockMvc.perform(delete("/api/members/delete/{memberId}", memberId)
                        .contentType(contentType)
                        .session(session))
                .andExpect(status().isOk());

        // then
        verify(memberService, times(1)).delete(memberId);
    }

    @Test
    @DisplayName("[DELETE] 회원 삭제 - 회원이 존재하지 않음")
    public void 회원_삭제_회원_존재_X() throws Exception {
        // given
        final Long memberId = member.getId();

        doThrow(new MemberNotFoundException(memberId)).when(memberService).delete(memberId);

        given(memberService.findOne(any()))
                .willReturn(member);

        // 로그인 세션 생성
        final MockHttpSession session = getSession(member);

        // when
        mockMvc.perform(delete("/api/members/delete/{memberId}", memberId)
                        .contentType(contentType)
                        .session(session))
                .andExpect(status().isBadRequest());

        // then
        verify(memberService, times(1)).delete(memberId);
    }

    @Test
    @DisplayName("[DELETE] 회원 삭제 - 로그인 X")
    public void 회원_삭제_로그인_세션_유효_X() throws Exception {
        // given
        final Long memberId = member.getId();

        final MockHttpSession session = getSession(null);

        // when
        mockMvc.perform(delete("/api/members/delete/{memberId}", memberId)
                        .session(session)
                        .contentType(contentType))
                .andExpect(status().is3xxRedirection()); //로그인이 되어 있지 않다면 로그인 페이지로 Redirect
    }

    @Test
    @DisplayName("[DELETE] 회원 삭제 - 회원 정보 불일치")
    public void 회원_삭제_회원_정보_불일치() throws Exception {
        final Long memberId = member.getId();

        doThrow(new UserException(ErrorCode.USER_MISMATCH)).when(memberService).loginValidation(any(), any());

        given(memberService.findOne(any()))
                .willReturn(member);

        // 로그인 세션 생성
        final MockHttpSession session = getSession(member);

        // when
        ResultActions actions = mockMvc.perform(delete("/api/members/delete/{memberId}", memberId)
                .contentType(contentType)
                .session(session));

        // then
        actions
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("회원 정보가 불일치합니다."))
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.code").value("A004"));
        verify(memberService, times(1)).loginValidation(any(), any());
    }
}