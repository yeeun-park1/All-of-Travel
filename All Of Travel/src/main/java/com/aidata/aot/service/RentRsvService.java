package com.aidata.aot.service;

import com.aidata.aot.dao.RReservationDao;
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
import java.util.List;

@Service
@Slf4j
public class RentRsvService {
    @Autowired
    private RReservationDao rDao;

    //트랜잭션 관련 객체 선언
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;

    public ModelAndView getRentList(PageDto page) {
        log.info("getRentCompany()");
        ModelAndView mv = new ModelAndView();

        //게시글 번호로 선택한 게시물 가져오기
        List<RentDto> rent = rDao.selectRentList(page);
        //DB에서 가져온 데이터를 mv에 담기
        mv.addObject("rent", rent);


        mv.setViewName("RentList");
        return mv;
    }


    public ModelAndView getRentDetail(int company, RentDto rentcompany) {
        log.info("getRentDetail()");
        ModelAndView mv = new ModelAndView();

        //게시글 번호로 선택한 게시물 가져오기
        List<clistDto> car = rDao.selectCarList(company);
        //DB에서 가져온 데이터를 mv에 담기
        mv.addObject("car", car);

        //게시글의 파일목록 가져오기
        List<RentFileDto> rfList = rDao.selectFileList(company);
        //DB에서 가져온 데이터를 mv에 담기
        mv.addObject("rfList", rfList);

        //API 주소 위도경도 / rnum rname 가져오기
        rentcompany = rDao.selectTheRent(company);
        mv.addObject("rentcompany", rentcompany);

        mv.setViewName("RentDetail");
        return mv;
    }

    public ModelAndView getCarDetail(int cnum) {
        log.info("getCarDetail()");
        ModelAndView mv = new ModelAndView();

        //게시글 내용 가져오기
        clistDto cdto = rDao.selectCarDetail(cnum);
        mv.addObject("cdto", cdto);

        //게시글의 파일목록 가져오기
       String coriname = rDao.selectCarFile(cnum);
        //DB에서 가져온 데이터를 mv에 담기
        mv.addObject("coriname", coriname);

        mv.setViewName("CarDetail");
        return mv;
    }



    public String calPayment(HttpSession session, rbookDto rbook, Model model, RedirectAttributes rttr) {
        log.info("calPayment()");
        String view = null;
        String msg = null;
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        if(member != null){

            model.addAttribute("rbook", rbook);
            model.addAttribute("member", member);
            long daysDifference = ChronoUnit.DAYS.between(rbook.getSdate(),rbook.getEdate());
            model.addAttribute("totalDate", daysDifference);
            long totalPrice = (rbook.getRprice() + rbook.getCinsuprice()) * daysDifference;
            model.addAttribute("totalPrice", totalPrice);

            view = "RPayment";
            msg = "결제 페이지로 이동합니다";
        }else {
            view = "redirect:Login";
            msg = "로그인을 진행해주세요";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public String RBookProc(rbookDto rbook, RedirectAttributes rttr) {
        log.info("rbookProc()");

        //트랜잭션 상태 처리 객체
        TransactionStatus status = manager.getTransaction(definition);

        String view = null;
        String msg = null;


        try{
            rDao.insertRbook(rbook);

            manager.commit(status); //최종승인

            view = "redirect:RBookDetail?book=" + rbook.getRbnum();
            msg = "예약완료! paymentMethod+\"로 예약금 10,000원을 입금하여주세요. \n 잔금 (" +
            rbook.getTotalprice()+" - 10,000) = "+(rbook.getTotalprice()-10000) +"은 현장결제 해주세요.";
        } catch (Exception e){
            e.printStackTrace();

            manager.rollback(status);//취소

            view = "redirect:RPayment";
            msg = "결제 실패";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;
    }

    public ModelAndView RBookDetail(int rbnum, HttpSession session) {
        log.info("rBookDetail()");
        ModelAndView mv = new ModelAndView();

        //게시글 번호로 선택한 게시물 가져오기
        rbookDto rbook = rDao.selectRbook(rbnum);
        mv.addObject("rbook", rbook);


        MembershipDto member = (MembershipDto) session.getAttribute("member");
        mv.addObject("member", member);

        mv.setViewName("RBookDetail");

        return mv;
    }


    public String RBookDelete(int rbnum, HttpSession session, RedirectAttributes rttr) {
        log.info("RBookDelete()");
        //트랜잭션
        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        String mid = member.getMid();

        try{
            rDao.deleteRbook(rbnum);
            manager.commit(status);
            view = "redirect:PreBookList?mid=" + mid;
            msg = "예약이 취소 되었습니다. 환불 금액은 은행 영업일 기준 3-5일 안에 고객님 계좌로 입금될 예정입니다.";

        }catch (Exception e){
            e.printStackTrace();

            manager.rollback(status);

            view = "RBookDetail?rbnum=" + rbnum;
            msg = "예약 취소 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;

    }


    public ModelAndView RBookChange(int rbnum, HttpSession session) {
        log.info("rBookChange()");
        ModelAndView mv = new ModelAndView();

        //게시글 내용 가져오기

        rbookDto rbook = rDao.selectRbook(rbnum);

        MembershipDto member = (MembershipDto) session.getAttribute("member");
        mv.addObject("member", member);
        mv.addObject("rbook", rbook);

        mv.setViewName("RBookChange");

        return mv;
    }

    public String RBookUpdateProc(rbookDto rbook) {
        log.info("rBookUpdateProc()");
        String view = null;

        try{
            String insu = rDao.selectInsu(rbook.getCinsuprice());
            rbook.setCinsu(insu);

            int totaldate = rDao.selectTotaldate(rbook.getRbnum());
            int rprice = rDao.selectRprice(rbook.getRbnum());
            int totalPrice = rbook.getTotalinsu() + (rprice * totaldate);
            int totalinsu = totalPrice + rbook.getCinsuprice();

            rbook.setTotalprice(totalinsu + totalPrice);

            rDao.updateRbook(rbook);
            view = "redirect:RBookDetail?book=" + rbook.getRbnum();
        } catch (Exception e) {
            e.printStackTrace();
            view = "redirect:RBookChange?rbnum=" + rbook.getRbnum();
        }

        return view;
    }
}