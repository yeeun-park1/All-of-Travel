package com.aidata.aot.controller;

import com.aidata.aot.dto.HotelDto;
import com.aidata.aot.dto.HotelFileDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.RoomDto;
import com.aidata.aot.service.HotelRsvService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@Slf4j
public class HReservationController {
    @Autowired
    HotelRsvService hserv;

    @GetMapping("index")
    public String index(){
        log.info("index()");
        return "index";
    }
    @GetMapping("HotelList")
    public ModelAndView HotelList(PageDto page){
        log.info("HotelList()");
        ModelAndView mv = hserv.HotelList(page);
        return mv;
    }
    @GetMapping("HotelDetail")
    public ModelAndView HotelDetail(int hnum){
        log.info("HotelDetail()");
        ModelAndView mv = hserv.HotelDetail(hnum);
        return mv;
    }
}
