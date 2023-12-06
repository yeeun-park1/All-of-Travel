package com.aidata.aot.controller;

import com.aidata.aot.dto.PageDto;
import com.aidata.aot.service.HotelRsvService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class ReservationController {
    @Autowired
    HotelRsvService hserv;

    @GetMapping("index")
    public String index(){
        log.info("index()");
        return "index";
    }
    @GetMapping("HotelList")
    public ModelAndView HotelList(HttpSession session, PageDto page){
        log.info("HotelList()");
        ModelAndView mv = hserv.HotelList(session, page);
        return mv;
    }
}
