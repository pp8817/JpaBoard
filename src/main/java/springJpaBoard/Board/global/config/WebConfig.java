package springJpaBoard.Board.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springJpaBoard.Board.global.argumenresolver.LoginMemberArgumentResolver;
import springJpaBoard.Board.global.interceptor.LogInterceptor;
import springJpaBoard.Board.global.interceptor.LoginCheckInterceptor;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/*", "/*.ico", "error", "/error-page/**");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/css/**", "/*.ico", "/error", "/error-page/**",
                        "/members/new", "/members/login", "/members", "/members/logout",
                        "/boards", "/boards/{boardId}/detail",

                        "/api/members/login", "/api/members", "/api/members/logout",
                        "/api/boards/list", "/api/boards/detail/**"
                );
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
