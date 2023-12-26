package com.aidata.aot.controller;

import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.RentDto;
import com.aidata.aot.dto.rbookDto;
import com.aidata.aot.service.RentRsvService;
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
public class RReservationController {
    @Autowired
    private RentRsvService RentServ;

    //회사정보 리스트 출력
    @GetMapping("RentList")
    public ModelAndView RentList(PageDto page) {
        log.info("RentList()");
        ModelAndView mv = RentServ.getRentList(page);
        return mv;
    }

    //선택회사와 차량종류 출력
    @GetMapping("RentDetail")
    public ModelAndView RentDetail(int company, RentDto rentcompany) {
        log.info("RentDetail()");
        ModelAndView mv = RentServ.getRentDetail(company, rentcompany);
        return mv;
    }

    //선택 차량 정보 출력
    @GetMapping("CarDetail")
    public ModelAndView CarDetail(int cnum) {
        log.info("CarDetail()");
        ModelAndView mv = RentServ.getCarDetail(cnum);
        return mv;
    }


    @PostMapping("RPaymentProc")
    public String RPaymentProc(HttpSession session, rbookDto rbook, Model model, RedirectAttributes rttr) {
        log.info("rPaymentProc()");

        String view = RentServ.calPayment(session, rbook, model, rttr);

        return view;
    }

    @PostMapping("RBookProc")
    public String RBookProc(rbookDto rbook, RedirectAttributes rttr) {
        log.info("rBookProc()");
        String view = RentServ.RBookProc(rbook, rttr);
        return view;
    }

    @GetMapping("RBookDetail")
    public ModelAndView RBookDetail(int book, HttpSession session) {
        log.info("rBookDetail()");
        ModelAndView mv = RentServ.RBookDetail(book, session);
        return mv;
    }
    //예약변경 페이지
    @GetMapping("RBookChange")
    public ModelAndView RBookChange(int rbnum, HttpSession session){
        log.info("rBookChange()");
        ModelAndView mv = RentServ.RBookChange(rbnum, session);
        return mv;
    }


    @GetMapping("RBookDelete")
    public String RBookDelete(int rbnum, HttpSession session, RedirectAttributes rttr){
        log.info("rBookDelete()");
        String view = RentServ.RBookDelete(rbnum, session, rttr);
        return view;
    }

    @PostMapping("RBookUpdateProc")
    public String RBookUpdateProc(rbookDto rbook){
        log.info("rBookUpdateProc()");
        String view = RentServ.RBookUpdateProc(rbook);
        return view;
    }


}