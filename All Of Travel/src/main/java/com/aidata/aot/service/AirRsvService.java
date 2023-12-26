package com.aidata.aot.service;

import com.aidata.aot.dao.AReservationDao;
import com.aidata.aot.dao.PersonalBookingDao;
import com.aidata.aot.dto.*;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
@Slf4j
public class AirRsvService {
    @Autowired
    private AReservationDao adao;
    //Transaction 관련 객체 생성

    @Autowired
    private PersonalBookingDao pdao;

    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;


    public ModelAndView AirLineList(PageDto page) {
        log.info("AirLineList()");

        ModelAndView mv = new ModelAndView();

        //DAO로 항공사 리스트 가져와서 mv에 담기
        List<AirLineDto> allist = adao.selectAirLine(page);
        mv.addObject("allist", allist);

        mv.setViewName("AirLineList");
        return mv;
    }

    public ModelAndView FlightList(int company, AirLineDto airline) {
        log.info("FlightList()");

        ModelAndView mv = new ModelAndView();

        //DAO로 항공편 리스트 가져와서 mv에 담기
        List<FlightDto> flist = adao.selectFlightList(company);
        mv.addObject("flist", flist);

        airline = adao.selectTheAirLine(company);
        mv.addObject("airline", airline);

        mv.setViewName("FlightList");
        return mv;

    }

    public ModelAndView FlightDetail(int fnum, FlightDto flight, AirLineDto airline) {
        log.info("FlightDetail()");

        ModelAndView mv = new ModelAndView();

        flight = adao.selectFlight(fnum);
        mv.addObject("flight", flight);

        mv.setViewName("FlightDetail");
        return mv;
    }

    public String calPayment(HttpSession session, aBookDto abook, Model model, RedirectAttributes rttr) {
        log.info("calPayment()");
        String view = null;
        String msg = null;
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        if (member != null) {
            model.addAttribute("abook", abook);
            model.addAttribute("member", member);

            long totalprice = abook.getFprice() * abook.getAbcount();
            model.addAttribute("totalprice", totalprice);
            view = "aPayment";
            msg = "결제페이지로 이동합니다";
        } else {
            view = "redirect:Login";
            msg = "로그인을 진행해주세요";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }


    public String aBookProc(aBookDto abook, RedirectAttributes rttr) {
        log.info("aBookProc()");
        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;
        try {
            log.info("예약번호 :" + abook.getAbnum());
            adao.insertAbook(abook);

            log.info("예약번호 : " + abook.getAbnum());

            manager.commit(status);
            view = "redirect:ABookDetail?book=" + abook.getAbnum();
            msg = "예약완료! paymentMethod+\"로 예약금 10,000원을 입금하여주세요. \n 잔금 (" +
                    abook.getTotalprice() + " - 10,000) = " + (abook.getTotalprice() - 10000) + "은 현장결제 해주세요.";

        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);
            view = "redirect:aPayment";
            msg = "결제실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }


    public ModelAndView ABookDetail(int abnum, HttpSession session) {
        log.info("ABookDetail()");
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        ModelAndView mv = new ModelAndView();
        aBookDto ab = adao.selectAbook(abnum);
        mv.addObject("ab", ab);
        mv.addObject("member", member);

        mv.setViewName("ABookDetail");
        return mv;

    }

    public String ABookUpdateProc(aBookDto abook) {
        log.info("ABookUpdateProc()");
        String view = null;
//        int fnum = adao.selectfnum(abook);
//        abook.setFnum(fnum);

        try{
            adao.updateAbook(abook);
            view="redirect:ABookDetail?book="+abook.getAbnum();
        } catch (Exception e){
            view="redirect:ABookChange?abnum="+abook.getAbnum();
        }


        return view;

    }

    public String ABookDelete(int abnum, HttpSession session, RedirectAttributes rttr) {
        log.info("ABookDelete()");
        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        String mid = member.getMid();
        try{
            adao.deleteAbook(abnum);
            manager.commit(status);
            view="redirect:PreBookList?mid="+mid;
            msg="예약이 취소 완료되었습니다. 환불 금액은 은행 영업일 기준 3-5일 안에 고객님 계좌로 입금될 예정입니다.";
        }catch (Exception e){
            e.printStackTrace();
            view = "ABookDetail?abnum="+abnum;
            msg = "예약 취소 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
}

    public ModelAndView ABookChange(int abnum, HttpSession session) {
        log.info("ABookChange()");
        ModelAndView mv = new ModelAndView();
        //게시글 내용 가져오기

        aBookDto abook = adao.selectAbook(abnum);

        MembershipDto member = (MembershipDto) session.getAttribute("member");
        mv.addObject("member", member);
        mv.addObject("abook", abook);

        mv.setViewName("ABookChange");
        return mv;
    }
}