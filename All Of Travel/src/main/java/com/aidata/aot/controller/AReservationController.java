package com.aidata.aot.controller;

import com.aidata.aot.dto.AirLineDto;
import com.aidata.aot.dto.FlightDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.aBookDto;
import com.aidata.aot.service.AirRsvService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class AReservationController {
    @Autowired
    AirRsvService aserv;

    @GetMapping("AirLineList")
    public ModelAndView AirLineList(PageDto page) {
        log.info("AirLineList()");
        ModelAndView mv = aserv.AirLineList(page);
        return mv;
    }//anum =company

    @GetMapping("FlightList")
    public ModelAndView FlightList(int company, AirLineDto airline) {
        log.info("FlightList()");
        ModelAndView mv = aserv.FlightList(company, airline);
        return mv;
    }

    @GetMapping("FlightDetail")
    public ModelAndView FlightDetail(int fnum, FlightDto flight, AirLineDto airline) {
        log.info("FlightDetail()");
        ModelAndView mv = aserv.FlightDetail(fnum, flight, airline);
        return mv;
    }

    @PostMapping("aPaymentProc")
    public String aPaymentProc(HttpSession session, aBookDto abook, Model model, RedirectAttributes rttr) {
        log.info("aPaymentProc");

        String view = aserv.calPayment(session, abook, model, rttr);

        return view;
    }
    @PostMapping("aBookProc")
    public String aBookProc(aBookDto abook, RedirectAttributes rttr){
        log.info("aBookProc()");
        String view = aserv.aBookProc(abook, rttr);
        return view;
    }
    @GetMapping("ABookDetail")
    public ModelAndView ABookDetail(int book,  HttpSession session){
        log.info("ABookDetail()");
        ModelAndView mv = aserv.ABookDetail(book, session);
        return mv;
    }
    @GetMapping("ABookDelete")
    public String ABookDelete(int abnum, HttpSession session, RedirectAttributes rttr){
        log.info("ABookDelete()");
        String view = aserv.ABookDelete(abnum, session, rttr);
        return view;
    }
    @GetMapping("ABookChange")
    public ModelAndView ABookChange(int abnum, HttpSession session) {
        log.info("ABookChange()");
        ModelAndView mv = aserv.ABookChange(abnum, session);
        return mv;
    }
    @PostMapping("ABookUpdateProc")
    public String ABookUpdateProc(aBookDto abook){
        log.info("ABookUpdateProc()");
        String view = aserv.ABookUpdateProc(abook);
        return view;
    }

}