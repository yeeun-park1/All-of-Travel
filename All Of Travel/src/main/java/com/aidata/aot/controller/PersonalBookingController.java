package com.aidata.aot.controller;

import com.aidata.aot.dto.EvaluateDto;
import com.aidata.aot.dto.LBookDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.service.PsvService;
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
public class PersonalBookingController {
    @Autowired
    private PsvService pServ;

    @GetMapping("Evaluate")
    public ModelAndView Evaluate(PageDto pdto){
        log.info("Evaluate()");
        ModelAndView mv = pServ.getEval( pdto);
        return mv;
    }

    @GetMapping("EvaluateWrite")
    public String EvaluateWrite(int company, String category, Model model,PageDto pdto){
        log.info("EvaluateWrite()");
        model.addAttribute("company", company);
        model.addAttribute("category", category);
        return "EvaluateWrite";
    }

    @PostMapping("EvaluateProc")
    public String EvaluateProc(HttpSession session, EvaluateDto edto, RedirectAttributes rttr, PageDto pdto){
        log.info("EvaluateProc()");
        String view = pServ.EvaluateWrite(edto, session, rttr, pdto);
        return view;
    }

    //재영 leisure
    @GetMapping("PreBookList")
    public ModelAndView PreBookList(PageDto pdto, HttpSession session){
        log.info("PreBookList()");
        ModelAndView mv = pServ.getPreBookList(pdto, session);
        return mv;
    }

    @GetMapping("ProBookList")
    public ModelAndView ProBookList(PageDto pdto, HttpSession session){
        log.info("ProBookList()");
        ModelAndView mv = pServ.getProBookList(pdto, session);
        return mv;
    }



}
