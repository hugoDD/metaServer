package cn.granitech.interceptor;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor getLoginInterceptor() {
        return new LoginInterceptor();
    }

    @Bean
    public ApiAuthInterceptor getApiAuthInterceptor() {
        return new ApiAuthInterceptor();
    }

    @Bean
    public WebInterceptor getWebInterceptor() {
        return new WebInterceptor();
    }

    public void addInterceptors(InterceptorRegistry registry) {
        String[] excludes = {"/error", "/web/**", "/**/*.js", "/**/*.css", "/assets/**"};
        registry.addInterceptor(getLoginInterceptor()).addPathPatterns("/*/**").excludePathPatterns(excludes);
        registry.addInterceptor(getApiAuthInterceptor()).addPathPatterns("/metaApi/*").excludePathPatterns(excludes);
        registry.addInterceptor(getWebInterceptor()).addPathPatterns("/web/**");
//       super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean<RequestBodyFilter> httpServletRequestReplacedFilter() {
        FilterRegistrationBean<RequestBodyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestBodyFilter());
        registration.addUrlPatterns("/metaApi/*");
        registration.setOrder(1);
        return registration;
    }

    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }
}
