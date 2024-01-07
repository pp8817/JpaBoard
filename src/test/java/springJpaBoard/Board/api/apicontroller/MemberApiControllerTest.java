package springJpaBoard.Board.api.apicontroller;


import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import springJpaBoard.Board.controller.requestdto.MemberRequestDTO;
import springJpaBoard.Board.domain.Address;
import springJpaBoard.Board.domain.Member;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("[POST] 회원 가입")
    public void 회원가입() throws Exception {
        //given
        given(memberService.join(any()))
                .willReturn(1L);

        //when
        ResultActions actions = mockMvc.perform(post("/api/members")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{" +
                        "   \"loginId\" : \"1\"," +
                        "    \"password\" : \"1\"," +
                        "    \"name\": \"1\"," +
                        "    \"gender\": \"남성\"," +
                        "    \"city\": \"1\"," +
                        "    \"street\": \"1\"," +
                        "    \"zipcode\": \"1\"" +
                        "}")
        );
        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.message").value("회원 가입 성공"))
                .andExpect(jsonPath("$.data").value("1"));

    }

    @Test
    @DisplayName("[POST] 로그인")
    public void 로그인() throws Exception {
        Member member = getMember();

        //given
        given(memberService.login("1", "1"))
                .willReturn(member);

        //when
        ResultActions actions = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{" +
                        "   \"loginId\" : \"1\"," +
                        "    \"password\" : \"1\"" +
                        "}")
        );
        //then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data").value("1"));

    }

    @Test
    @DisplayName("[POST] 로그인 실패 - 검증")
    public void 로그인_실패_검증() throws Exception {
        Member member = getMember();

        //given
        given(memberService.login("1", "1"))
                .willReturn(member);

        //when
        ResultActions actions = mockMvc.perform(post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content("{" +
                        "    \"password\" : \"1\"" +
                        "}")
        );
        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("USER-EX"))
                .andExpect(jsonPath("$.message").value("로그인: 아이디 또는 비밀번호 오류"));
    }



    @NotNull
    private static Member getMember() {
        Address address = new Address("1", "1", "1");
        MemberRequestDTO memberRequestDTO = new MemberRequestDTO();
        memberRequestDTO.setName("1");
        memberRequestDTO.setGender("남성");
        memberRequestDTO.setLoginId("1");
        memberRequestDTO.setPassword("1");
        Member member = new Member();
        member.createMember(memberRequestDTO, address);
        return member;
    }

}