package com.aidata.springboard3.controller;

import com.aidata.springboard3.dto.HospitalDto;
import com.aidata.springboard3.dto.SearchDto;
import com.aidata.springboard3.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class HospitalController {

    @Autowired
    private BoardService bserv;

    @GetMapping ("HosptialList")
    public ModelAndView HosptialList(SearchDto sdto, HttpSession session, HospitalDto hdto){
        log.info("HosptialList()");
        ModelAndView mv = bserv.HostpitalList(sdto, session, hdto);
        return mv;
    }
    @GetMapping("tvList")
    public String tvList(){
        log.info("tvList()");
        return "tvList";
    }
}
