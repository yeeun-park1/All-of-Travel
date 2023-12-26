package com.aidata.aot.controller;

import com.aidata.aot.dto.BookmarkDto;
import com.aidata.aot.dto.MembershipDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.service.MembershipService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
public class MembershipController {
    @Autowired
    private MembershipService mServ;


    @GetMapping("Login")
    public String Login(){
        log.info("Login()");
        return "Login";
    }
    @GetMapping("Signup")
    public String Signup(){
        log.info("Signup()");
        return "Signup";
    }
    @PostMapping("loginProc")
    public String loginProc(MembershipDto member, HttpSession session, RedirectAttributes rttr){
        log.info("loginProc()");
        String view = mServ.loginProc(member, session, rttr);
        return view;
    }
    @PostMapping("signupProc")
    public String signupProc(MembershipDto member, RedirectAttributes rttr){
        log.info("signupProc()");
        String view = mServ.memberJoin(member, rttr);
        return view;
    }
    @GetMapping("Logout")
    public String logout(HttpSession session){
        log.info("logout()");
        String view = mServ.logout(session);
        return view;
    }

    @GetMapping("MyPage")
    public String MyPage(){
        log.info("MyPage()");
        return "MyPage";
    }
    @GetMapping("MypageUpdate")
    public String MypageUpdate(){
        log.info("MypageUpdate()");
        return "MypageUpdate";
    }

    @PostMapping("m_Update")
    public String m_Update(MembershipDto member, HttpSession session, RedirectAttributes rttr){
        log.info("m_Update()");
        String view = mServ.m_Update(member, session, rttr);
        return view;
    }
    @GetMapping("BookmarkList")
    public ModelAndView BookmarkList(HttpSession session, PageDto pdto){
        log.info("BookmarkList()");
        ModelAndView mv = mServ.BookmarkList(session, pdto);
        return mv;
    }
    @GetMapping("findPw")
    public String findPw(){
        log.info("findPw()");
        return "findPw";
    }

    @GetMapping("pwUpdate")
    public String pwUpdate(String mid, Model model){
        log.info("pwUpdate()");
        model.addAttribute("mid", mid);
        return "pwUpdate";
    }

    @PostMapping("findPwUpdateProc")
    public String findPwUpdateProc(MembershipDto member, HttpSession session, RedirectAttributes rttr){
        log.info("findPwUpdateProc()");
        String result = mServ.findPwUpdateProc(member, session, rttr);
        return result;
    }

    @GetMapping("findId")
    public String findId(){
        log.info("findId()");
        return "findId";
    }

    @PostMapping("findIdProc")
    public String findIdProc (String mlname, String mfname, String mphone, HttpSession session, RedirectAttributes rttr){
        log.info("findIdProc()");
        String result = mServ.mfindId(mlname, mfname, mphone, session, rttr);
        return result;
    }

    @GetMapping("findIdView")
    public String findIdView(){
        log.info("findIdView()");
        return "findIdView";
    }
}
