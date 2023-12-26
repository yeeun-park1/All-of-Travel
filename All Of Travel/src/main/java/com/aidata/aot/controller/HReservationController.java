package com.aidata.aot.controller;

import com.aidata.aot.dao.HReservationDao;
import com.aidata.aot.dto.*;
import com.aidata.aot.service.HotelRsvService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;

@Controller
@Slf4j
public class HReservationController {
    @Autowired
    HotelRsvService hserv;
    @Autowired
    HReservationDao rdao;


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
    public ModelAndView HotelDetail(int company,  HotelDto hotel){
        log.info("HotelDetail()");
        ModelAndView mv = hserv.HotelDetail(company, hotel);
        return mv;
    }
    @GetMapping("RoomDetail")
    public ModelAndView RoomDetail(int roomnum, RoomDto room){
        log.info("RoomDetail()");
        ModelAndView mv = hserv.RoomDetail(roomnum, room);
        return mv;
    }
    @PostMapping("hbookProc")
    public String hbookProc(HBookDto hbook, RedirectAttributes rttr){
        log.info("hbookProc()");
        String view = hserv.hbookProc(hbook, rttr);
        return view;
    }
    @PostMapping("HPaymentProc")
    public String HPaymentProc(HttpSession session, HBookDto hbook, Model model, RedirectAttributes rttr){
        log.info("HPaymentProc()");

        String view = hserv.calPayment(session, hbook, model, rttr);

        return view;
    }
    @GetMapping("HBookDetail")
    public ModelAndView HBookDetail(int book,  HttpSession session){
        log.info("HBookDetail()");
        ModelAndView mv = hserv.HBookDetail(book, session);
        return mv;
    }
    @GetMapping("HBookChange")
    public ModelAndView HBookChange(int hbnum, HttpSession session) {
        log.info("HBookChange()");
        ModelAndView mv = hserv.HBookChange(hbnum, session);
        return mv;
    }
    @GetMapping("HBookDelete")
    public String HBookDelete(int hbnum, HttpSession session, RedirectAttributes rttr){
        log.info("boardDelete()");
        String view = hserv.HBookDelete(hbnum, session, rttr);
        return view;
    }
    @PostMapping("HBookUpdateProc")
    public String HBookUpdateProc(HBookDto hbook){
        log.info("HBookUpdateProc()");
        String view = hserv.HBookUpdateProc(hbook);
        return view;
    }

}
