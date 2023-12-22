package springJpaBoard.Board;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springJpaBoard.Board.interceptor.LogInterceptor;
import springJpaBoard.Board.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/*", "/*.ico", "error");

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/members/new", "/members/login", "/members",
                        "/boards", "/boards/{boardId}/detail", "/members/logout", "/css/**",
                        "/*.ico", "/error"
                );
    }
}
