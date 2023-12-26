package com.aidata.aot.controller;


import com.aidata.aot.dto.LBookDto;
import com.aidata.aot.dto.MembershipDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.TlistDto;
import com.aidata.aot.service.LeisureRsvService;
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
public class LReservationController {
    @Autowired
    private LeisureRsvService lsrev;

    @GetMapping("LeisureList")
    public ModelAndView LeisureList(PageDto pdto){
        log.info("LeisureList()");
        ModelAndView mv = lsrev.LeisureList(pdto);
        return mv;
    }

    @GetMapping("LeisureDetail")
    public ModelAndView LeisureDetail(TlistDto tdto, int company){
        log.info("LeisureDetail() : {}", company);
        ModelAndView mv = lsrev.LeisureDetail(tdto,company);
        return mv;
    }
    @GetMapping("TicketDetail")
    public ModelAndView TicketDetail(TlistDto tdto, int tnum){
        log.info("LeisureDetail() : {}", tnum);
        ModelAndView mv = lsrev.TicketDetail(tdto,tnum);
        return mv;
    }

    @PostMapping("lpayProc")
    public String lpayProc(LBookDto lbook, HttpSession session, Model model){
        log.info("lpayProc()");
        model.addAttribute("lbook", lbook);
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        model.addAttribute("member", member);
        return "LPayment";
    }

    @PostMapping("lbookProc")
    public String lbookProc(LBookDto lbook, RedirectAttributes rttr){
        log.info("lbookProc()");
        String view = lsrev.lbookjoin(lbook,rttr);
        return view;
    }

    @GetMapping("LBookDetail")
    public ModelAndView LBookDetail(int book){
        log.info("LBookDetail()");
        ModelAndView mv = lsrev.LBookDetail(book);
        return mv;
    }

    @PostMapping("LBookChange")
    public ModelAndView LBookChange(int lnum, LBookDto lbook, HttpSession session){
        log.info("LBookChange");
        ModelAndView mv = lsrev.lbChangeProc(lnum, lbook, session);
        return mv;
    }

    @PostMapping("bChangeProc")
    public String bChangeProc(LBookDto lbook, RedirectAttributes rttr){
        log.info("bChangeProc()");
        String view = lsrev.bChangeProc(lbook, rttr);
        return view;
    }

    @GetMapping("LBookDelete")
    public String LBookDelete(int lbnum, RedirectAttributes rttr){
        log.info("LBookDelete()");
        String view = lsrev.LBookDelete(lbnum, rttr);
        return view;
    }

}
