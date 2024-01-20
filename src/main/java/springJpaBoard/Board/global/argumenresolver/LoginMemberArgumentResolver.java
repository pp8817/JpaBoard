package springJpaBoard.Board.global.argumenresolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import springJpaBoard.Board.global.constans.SessionConst;
import springJpaBoard.Board.domain.member.model.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    /**
     * @Login 애노테이션이 있으면서 Member 타입이면 해당 ArgumentResolver가 사용된다.
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class); // @Login이 붙어 있는지
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType()); // Member 타입인지

        return hasLoginAnnotation && hasMemberType; //@Login Member member 인지 체크
    }

    /**
     * 컨트롤러 호출 직전에 호출 되어서 필요한 파라미터 정보를 생성해준다.
     * 세션에 있는 로그인 회원 정보인 member 객체를 찾아서 반환해준다.
     * 이후 스프링 MVC는 컨트롤러의 메서드를 호출하면서 여기에서 반환된 member 객체를 파라미터에 전달해준다.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
