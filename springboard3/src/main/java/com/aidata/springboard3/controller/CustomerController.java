package com.aidata.springboard3.controller;

import com.aidata.springboard3.dao.CustomerDao;
import com.aidata.springboard3.dto.CustomerDto;
import com.aidata.springboard3.dto.ReviewDto;
import com.aidata.springboard3.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class CustomerController {
    @Autowired
    private CustomerService cserv;
    @Autowired
    private CustomerDao cdao;

    @GetMapping("/")
    public String home(){
        log.info("home()");
        return "index";
    }
    @GetMapping("LoginForm")
    public String LoginForm(){
        log.info("LoginForm()");
        return "LoginForm";
    }

    @PostMapping("loginProc")
    public String loginProc(CustomerDto customer, RedirectAttributes rttr, HttpSession session){
        log.info("loginProc()");
        String view = cserv.loginProc(customer, rttr, session);
        return view;
    }

    @GetMapping("JoinForm")
    public String JoinForm(){
        log.info("JoinForm()");
        return "JoinForm";
    }

    @PostMapping("JoinProc")
    public String JoinProc(CustomerDto customer, RedirectAttributes rttr){
        log.info("JoinProc()");
        String view = cserv.joinProc(customer, rttr);
        return view;
    }

    @GetMapping("Logout")
    public String Logout(HttpSession session){
        log.info("Logout()");
        cserv.logout(session);
        return "redirect:/";
    }

    @GetMapping("myPage")
    public String myPage(){
        log.info("myPage()");

        return "myPage";
    }
    @GetMapping("Delete")
    public String Delete(HttpSession session, RedirectAttributes rttr, ReviewDto rdto, String cid){
        log.info("Delete()");
        cserv.deleteMember(session, rttr, rdto, cid);
        return "Delete";
    }
}
