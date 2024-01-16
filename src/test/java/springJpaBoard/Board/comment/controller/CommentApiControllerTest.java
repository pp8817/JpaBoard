package springJpaBoard.Board.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import springJpaBoard.Board.api.apicontroller.CommentApiController;
import springJpaBoard.Board.service.BoardService;
import springJpaBoard.Board.service.CommentService;
import springJpaBoard.Board.service.MemberService;

import java.nio.charset.StandardCharsets;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CommentApiController.class)
@DisplayName("CommentApiController 테스트")
public class CommentApiControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BoardService boardService;
    
    @MockBean
    private CommentService commentService;
    
    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    protected MediaType contentType =
            new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

//    @Test
//    public void () throws Exception {
//        //given
//
//        //when
//
//        //then
//    }
}
