package com.aidata.aot.service;

import com.aidata.aot.dao.HReservationDao;
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

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HotelRsvService {
    @Autowired
    private HReservationDao rdao;
    @Autowired
    private PersonalBookingDao pdao;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;

    public ModelAndView HotelList(PageDto page) {
        log.info("HotelList()");

        ModelAndView mv = new ModelAndView();

        //DAO로 호텔 리스트 가져와서 mv에 담기
        List<HotelDto> hlist = rdao.selectHotel(page);
        mv.addObject("hlist", hlist);

        mv.setViewName("HotelList");
        return mv;
    }

    public ModelAndView HotelDetail(int company, HotelDto hotel) {
        log.info("HotelDetail()");

        ModelAndView mv = new ModelAndView();

        //DAO로 룸 리스트 가져와서 mv에 담기
        List<RoomDto> rlist = rdao.selectRoomList(company);
        mv.addObject("rlist", rlist);

        //API 주소 위도 경도 / hnum hname 가져오기
        hotel = rdao.selectTheHotel(company);
        mv.addObject("hotel", hotel);

        //DAO로 룸 파일 가져오기
        List<HotelFileDto> hfList = rdao.selectHFileList(company);
        mv.addObject("hfList", hfList);
        mv.setViewName("Hoteldetail");
        return mv;
    }

    public ModelAndView RoomDetail(int roomnum, RoomDto room) {
        log.info("RoomDetail()");

        ModelAndView mv = new ModelAndView();

        room = rdao.selectRoom(roomnum);
        mv.addObject("room", room);

        mv.setViewName("RoomDetail");
        return mv;
    }
    public String calPayment(HttpSession session, HBookDto hbook, Model model, RedirectAttributes rttr) {
        log.info("calPayment()");
        String view = null;
        String msg = null;
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        if(member != null) {
            model.addAttribute("hbook", hbook);
            model.addAttribute("member", member);
            long daysDifference = ChronoUnit.DAYS.between(hbook.getSdate(), hbook.getEdate());
            model.addAttribute("totalDate", daysDifference);
            long totalPrice = hbook.getHprice() * daysDifference;
            model.addAttribute("totalPrice", totalPrice);
            view = "HPayment";
            msg = "결제페이지로 이동합니다";
        }else {
            view = "redirect:Login";
            msg = "로그인을 진행해주세요";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public String hbookProc(HBookDto hbook, RedirectAttributes rttr) {
        log.info("hbookProc()");
        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;
        try{
            log.info("예약번호 :" + hbook.getHbnum());
            rdao.insertHbook(hbook);

            log.info("예약번호 : " + hbook.getHbnum());


            manager.commit(status);
            view = "redirect:HBookDetail?book="+hbook.getHbnum();
            msg="예약완료!\n" + hbook.getPaymentMethod() + " 으로 예약금 10,000원을 입금하여주세요. \n 잔금 (" +
                    hbook.getTotalPrice()+" - 10,000) = "+(hbook.getTotalPrice()-10000)+"은 현장결제 해주세요.";

        }catch (Exception e){
            e.printStackTrace();
            manager.rollback(status);
            view="redirect:HPayment";
            msg ="결제실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public ModelAndView HBookDetail(int book, HttpSession session) {
        log.info("HBookDetail()");
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        ModelAndView mv = new ModelAndView();
        HBookDto hb = rdao.selectHbook(book);
        mv.addObject("hb", hb);
        mv.addObject("member", member);

        mv.setViewName("HBookDetail");
        return mv;
    }

    public String HBookDelete(int hbnum, HttpSession session, RedirectAttributes rttr) {
        log.info("HBookDelete()");
        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        String mid = member.getMid();
        try{
            rdao.deleteHbook(hbnum);
            manager.commit(status);
            view="redirect:PreBookList?mid="+mid;
            msg="예약이 취소 완료되었습니다. 환불 금액은 은행 영업일 기준 3-5일 안에 고객님 계좌로 입금될 예정입니다.";
        }catch (Exception e){
            e.printStackTrace();
            view = "HBookDetail?hbnum="+hbnum;
            msg = "예약 취소 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public ModelAndView HBookChange(int hbnum, HttpSession session) {
        log.info("HBookChange()");
        ModelAndView mv = new ModelAndView();
        //게시글 내용 가져오기

        HBookDto hbook = rdao.selectHbook(hbnum);

        MembershipDto member = (MembershipDto) session.getAttribute("member");
        mv.addObject("member", member);
        int hmax = rdao.selectFhmax(hbook.getRoomnum());
        mv.addObject("hmax", hmax);
        mv.addObject("hbook", hbook);

        mv.setViewName("HBookChange");
        return mv;

    }

    public Map<String, Integer> roomChanged(HBookDto hbook) {
        log.info("roomChanged()");
        Integer hprice = rdao.selectRprice(hbook);
        int hmax = rdao.selectHmax(hbook);
        Map<String, Integer> hinfo = new HashMap<>();
        hinfo.put("hprice", hprice);
        hinfo.put("hmax", hmax);
        return hinfo;
    }

    public String HBookUpdateProc(HBookDto hbook) {
        log.info("HBookUpdateProc()");
        String view = null;
        int roomnum = rdao.selectRoomnum(hbook);
        hbook.setRoomnum(roomnum);
        rdao.updateHbook(hbook);

        view="redirect:HBookDetail?book="+hbook.getHbnum();
        return view;
    }
}
