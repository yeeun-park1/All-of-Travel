package com.aidata.aot.service;

import com.aidata.aot.dao.LReservationDao;
import com.aidata.aot.dao.PersonalBookingDao;
import com.aidata.aot.dto.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
@Slf4j
public class LeisureRsvService {
@Autowired
private LReservationDao ldao;



    public ModelAndView LeisureList(PageDto pdto) {
        log.info("getLeisureList()");
        ModelAndView mv = new ModelAndView();

        //레저상품가져오기
        List<TlistDto> tList = ldao.selectLeisureList(pdto);
        mv.addObject("tList", tList);

        mv.setViewName("LeisureList");
        return mv;
    }

    public ModelAndView LeisureDetail(TlistDto tdto, int company) {
        log.info("LeisureDetail()");
        ModelAndView mv = new ModelAndView();

        //티켓가져오기
        List<TlistDto> ticket = ldao.selectTicket(company);
        mv.addObject("ticket",ticket);

        //이미지파일가져오기
        List<LeisureFileDto> file = ldao.selectFile(company);
        mv.addObject("file",file);

        //상품이름 가져오기
        String tname = ldao.selecttname(company);
        mv.addObject("tname",tname);

        //이용정보 위치 회사이름 위도 경도 번호 카테고리 가져오기
        tdto = ldao.selectinfo(company);
        mv.addObject("tdto",tdto);

        mv.setViewName("LeisureDetail");
        return mv;
    }

    public ModelAndView TicketDetail(TlistDto tdto, int tnum) {
        log.info("TicketDetail()");
        ModelAndView mv = new ModelAndView();

        tdto = ldao.selectTicketinfo(tnum);
        mv.addObject("ticket",tdto);

        mv.setViewName("TicketDetail");
        return mv;
    }

    public String lbookjoin(LBookDto lbook, RedirectAttributes rttr) {
        log.info("lbookjoin()");
        String view = null;
        String msg = null;

        try {
            ldao.insertlbook(lbook);
            msg = "예약완료!\n" + lbook.getPaymentMethod() + " 으로 예약금 10,000원을 입금하여주세요. \n 잔금 (" +
                    lbook.getTtotalprice()+" - 10,000) = "+(lbook.getTtotalprice()-10000)+"은 현장결제 해주세요.";
            view = "redirect:LBookDetail?book="+lbook.getLbnum();
        } catch (Exception e) {
            e.printStackTrace();
            msg = "구매 실패";
            view = "redirect:TicketDetail";
        }
        rttr.addFlashAttribute("msg",msg);
        return view;
    }

    public ModelAndView LBookDetail(int book) {
        ModelAndView mv = new ModelAndView();

        LBookDto lbook = ldao.getLBookDetail(book);
        mv.addObject("lbook",lbook);

        return mv;
    }

    public ModelAndView lbChangeProc(int lnum, LBookDto lbook, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("lbook",lbook);
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        mv.addObject("member",member);
        List<TlistDto> ticket = ldao.getTicket(lnum);
        mv.addObject("ticket",ticket);

        mv.setViewName("LBookChange");

        return mv;
    }

    public String bChangeProc(LBookDto lbook, RedirectAttributes rttr) {
        String view = null;
        String msg = null;
        int price = 0;
        try{
            ldao.changeLBook(lbook);
            if(lbook.getTotalprice() > lbook.getTtotalprice()){
                price = lbook.getTotalprice() - lbook.getTtotalprice();
                view = "redirect:PreBookList?category=leisure";
                msg = "예약 변경 성공! 남은금액 " + price + "원은 결제하신 카드계좌로 입금됩니다.";
            } else if (lbook.getTotalprice() == lbook.getTtotalprice()){
                view = "redirect:PreBookList?category=leisure";
                msg = "예약 변경 성공";
            } else {
                price = lbook.getTtotalprice() - lbook.getTotalprice();
                view = "redirect:PreBookList?category=leisure";
                msg = "예약 변경 성공! 추가금액 " + price + "원은 현장에서 결제해주시기 바랍니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            view = "redirect:LBookChange?lnum=" + lbook.getLnum();
            msg = "예약 변경 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;

    }

    public String LBookDelete(int lbnum, RedirectAttributes rttr) {
        String view = null;
        String msg = null;

        try{
            ldao.deleteLBook(lbnum);
            view = "redirect:PreBookList?category=leisure";
            msg = "예약 취소 성공";
        } catch (Exception e) {
            e.printStackTrace();
//            + lbook.getLnum()
            view = "redirect:LBookChange?lnum=";
            msg = "예약 취소 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }
}
