package com.aidata.springboard3.config;

import com.aidata.springboard3.util.SessionIntercepter;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SessionIntercepter intercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(intercepter).addPathPatterns("/**")
                .excludePathPatterns("/", "/css/**", "/js/**")
                .excludePathPatterns("/image/**","/JoinForm","/LoginForm","/idcheck","/tvList","/HosptialList")
                .excludePathPatterns("/JoinProc", "/loginProc", "/error/**");
    }

}
