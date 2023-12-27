package com.aidata.springboard3.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class SessionIntercepter implements AsyncHandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle()");
        HttpSession session = request.getSession();

        if (session.getAttribute("customer") == null) {
            log.info("인터셉터 : 로그인 안함");
            response.sendRedirect("LoginForm");
            return false;
        }
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception{
        log.info("postHandle()");
        if(request.getProtocol().equals("HTTP/1.1")){
            response.setHeader("Cashe-Control", "no-cashe, no-store, must-revalidate");
        }else {
            response.setHeader("Pragma", "no-cashe");
        }

    }
}
