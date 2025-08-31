package app.eurocars._config;

import app.eurocars.security.SessionCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private final SessionCheckInterceptor sessionCheckInterceptor;

    public WebMvcConfiguration(SessionCheckInterceptor sessionCheckInterceptor) {
        this.sessionCheckInterceptor = sessionCheckInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(sessionCheckInterceptor)
               .addPathPatterns("/**")
               .excludePathPatterns("/styles/**", "/scripts/**", "/assets/**");

    }
}
