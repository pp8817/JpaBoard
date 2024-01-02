package springJpaBoard.Board.api.apicontroller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import springJpaBoard.Board.service.MemberService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberApiController.class)
@AutoConfigureMockMvc
@DisplayName("MemberApiController 테스트")
public class MemberApiControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new CharacterEncodingFilter("UTF-8", true)) // utf-8 필터 추가
                        .build();
    }


//    @Test
//    @DisplayName("리턴값 없이 테스트하는 방법")
//    void getTest() throws Exception {
//        String url = "~~";
//        // api 요청
//        mockMvc
//                .perform(
//                        get(url) // url
//                                .param("name", "myName") // parameter(파라미터 명, 데이터)
//                                .param("value", "myValue") // parameter
//                )
//                .andDo(print()) // api 수행내역 로그 출력
//                .andExpect(status().isOk()) // response status 200 검증
//                .andExpect(jsonPath("method").value("GET")) // response method 데이터 검증
//                .andExpect(jsonPath("name").value("myName")) // response name 데이터 검증
//                .andExpect(jsonPath("value").value("myValue")); // response value 데이터 검증
//    }

//    @Test
//    @DisplayName("리턴값을 이용하여 테스트하는 방법")
//    void getTestReturn() throws Exception {
//        // api 요청
//        MvcResult mvcResult = mockMvc
//                .perform(
//                        get("/api/mock/getTest") // url
//                                .param("name", "myName") // parameter
//                                .param("value", "myValue") // parameter
//                )
//                .andDo(print())
//                .andReturn();
//
//        // response 데이터 변환
//        Map<String, String> responseMap =
//                new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);
//
//        // 검증
//        Assertions.assertEquals(responseMap.get("method"), "GET"); // response method 데이터 검증
//        Assertions.assertEquals(responseMap.get("name"), "myName"); // response name 데이터 검증
//        Assertions.assertEquals(responseMap.get("value"), "myValue"); // response value 데이터 검증
//    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void 회원가입() throws Exception {
        //given

        //when

        //then


    }
}