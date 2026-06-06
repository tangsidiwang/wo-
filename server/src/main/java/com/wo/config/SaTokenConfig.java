package com.wo.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 不强制登录，只做 token 解析：有 token 则自动注入 StpUtil，无 token 则为游客
        registry.addInterceptor(new SaInterceptor())
                .addPathPatterns("/api/v1/**")
                .excludePathPatterns(
                        "/doc.html", "/swagger-ui/**", "/v3/api-docs/**"
                );
    }
}
