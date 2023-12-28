package com.aidata.aot.service;

import com.aidata.aot.dao.MembershipDao;
import com.aidata.aot.dao.PersonalBookingDao;
import com.aidata.aot.dto.*;
import com.aidata.aot.util.PagingUtil;
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
public class PsvService {
    @Autowired
    private PersonalBookingDao pDao;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;
    @Autowired
    private MembershipDao mDao;

    private String pre = "이용 전";
    private String pro = "이용 후";

    public ModelAndView getEval(PageDto pdto) {
        log.info("getEval()");

        ModelAndView mv = new ModelAndView();
        pdto.setListCnt(6);
        List<Integer> score = pDao.selectScore(pdto);

        int num = pdto.getPageNum();
        pdto.setPageNum((num-1)*pdto.getListCnt());

        List<EvaluateDto> elist = pDao.selectEval(pdto);
        mv.addObject("elist", elist);

        int count = pDao.selectCount(pdto);
        int total = 0;
        Float avg = 0.f;
        if (count != 0) {
            for (int c : score) {
                total += c;

            }
            avg = (float) total / count;
        }
        mv.addObject("avg", avg);
        mv.addObject("count", count);

        pdto.setPageNum(num);
        String paging = getPaging(pdto);
        mv.addObject("paging", paging);

        mv.setViewName("Evaluate");

        return mv;
    }

    private String getPaging(PageDto pdto) {
        String paging =null;

        int maxNum =  pDao.selectCount(pdto);

        int pageCnt = 5;

        String listName = "Evaluate?category="+ pdto.getCategory() +"&company="+ pdto.getCompany() +"&";

        PagingUtil pu = new PagingUtil(maxNum, pdto.getPageNum(), pdto.getListCnt(), pageCnt, listName);

        paging = pu.makePaging();

        return paging;
    }

    public String EvaluateWrite(EvaluateDto edto, HttpSession session, RedirectAttributes rttr, PageDto pdto) {
        log.info("EvaluateWrite()");

        TransactionStatus status = manager.getTransaction(definition);
        String view = null;
        String msg = null;

        pdto.setCategory(edto.getCategory());
        pdto.setCompany(edto.getCompany());

        try {
            //글 내용 저장
            log.info("게시글번호 :" + edto.getEvnum());
            pDao.insertEvaluate(edto);
            log.info("게시글번호 :" + edto.getEvnum());

            List<Integer> score = pDao.selectScore(pdto);
            int count = pDao.selectCount(pdto);
            int total = 0;
            Float avg = 0.f;
            if (count != 0) {
                for (int c : score) {
                    total += c;

                }
                avg = (float) total / count;
                edto.setAvg(avg);
            }
            switch (pdto.getCategory()) {
                case "hotel":
                    try{
                        pDao.updateHavg(edto);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "rent":
                    try{
                        pDao.updateRavg(edto);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "air":
                    try{
                        pDao.updateAavg(edto);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "leisure":
                    try{
                        pDao.updateLavg(edto);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
            }
        //작성자 point 수정
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        int point = member.getMpoint() + 10;

        if (point > 1000) {//point가 100을 넘지 않도록 필터링
            point = 1000;
        }

        member.setMpoint(point);
        mDao.updateMpoint(member);

        // 세션에 새 정보를 저장
        member = mDao.selectMember(member.getMid());
        session.setAttribute("member", member);
        // 세션에 같은 이름으로 set해서 덮어쓰기 (point가 update되서)

        manager.commit(status); //최종승인
        view = "redirect:Evaluate?category="+edto.getCategory()+"&company="+edto.getCompany();
        msg = "작성 성공";

    }catch(
    Exception e)

    {
        e.printStackTrace();
        manager.rollback(status); //최종승인 거부
        view = "redirect:EvaluateWrite";
        msg = "작성 실패";
    }
        rttr.addFlashAttribute("msg",msg);
        return view;
}
// lesisure - 재영
public ModelAndView getPreBookList(PageDto pdto, HttpSession session) {
    log.info("getPreBookList()");
    ModelAndView mv = new ModelAndView();

    if(pdto.getCategory() == null){
        pdto.setCategory("hotel");
    }

    int num = pdto.getPageNum();
    if(num ==0){
        num=1;
    }

    //pageNum을 LIMIT 시작 번호로 변경
    pdto.setPageNum((num - 1) * pdto.getListCnt());

    MembershipDto member = (MembershipDto) session.getAttribute("member");
    String mid = member.getMid();
    if (pdto.getMid() == null) {
        pdto.setMid(mid);
    }

    if (pdto.getStatus() == null) {
        pdto.setStatus(pre);
    }

    String url = null;
    List<PageDto> prelist = null;
    switch (pdto.getCategory()){
        case "hotel" :
            prelist =  pDao.selectHBookList(pdto);
            url = "/HBookDetail";
            break;
        case "air" :
            prelist =  pDao.selectABookList(pdto);
            url = "/ABookDetail";
            break;
        case "rent" :
            prelist =  pDao.selectRBookList(pdto);
            url = "/RBookDetail";
            break;
        case "leisure" :
            prelist =  pDao.selectLBookList(pdto);
            url = "/LBookDetail";
            break;
    }
    mv.addObject("url", url);
    mv.addObject("prelist", prelist);

    mv.setViewName("PreBookList");

    //페이징 처리
    pdto.setPageNum(num);//원래 페이지 번호로 환원
    String pageHtml = getprePaging(pdto);
    mv.addObject("paging", pageHtml);

    session.setAttribute("pageNum", num);

    mv.setViewName("PreBookList");
    return mv;
}

    private String getprePaging(PageDto pdto) {
        String pageHtml = null;

        if(pdto.getCategory() == null){
            pdto.setCategory("hotel");
        }

        int maxNum = 0;
        String listName = "";
        switch (pdto.getCategory()){
            case "hotel" :
                maxNum =  pDao.countHBookList(pdto);
                listName = "PreBookList?category=hotel&";
                break;
            case "air" :
                maxNum =  pDao.countABookList(pdto);
                listName = "PreBookList?category=air&";
                break;
            case "rent" :
                maxNum =  pDao.countRBookList(pdto);
                listName = "PreBookList?category=rent&";
                break;
            case "leisure" :
                maxNum =  pDao.countLBookList(pdto);
                listName = "PreBookList?category=leisure&";
                break;
        }

        //페이지에 보여질 번호 개수
        int pageCnt = 5;

        PagingUtil paging = new PagingUtil(
                maxNum,
                pdto.getPageNum(),
                pdto.getListCnt(),
                pageCnt,
                listName
        );

        pageHtml = paging.makePaging();

        return pageHtml;
    }


    public ModelAndView getProBookList(PageDto pdto, HttpSession session) {
        log.info("getProBookList()");
        ModelAndView mv = new ModelAndView();

        if(pdto.getCategory() == null){
            pdto.setCategory("hotel");
        }

        int num = pdto.getPageNum();
        if(num ==0){
            num=1;
        }

        //pageNum을 LIMIT 시작 번호로 변경
        pdto.setPageNum((num - 1) * pdto.getListCnt());

        MembershipDto member = (MembershipDto) session.getAttribute("member");
        String mid = member.getMid();
        if (pdto.getMid() == null) {
            pdto.setMid(mid);
        }

        if (pdto.getStatus() == null) {
            pdto.setStatus(pro);
        }

        String url = null;
        List<PageDto> prolist = null;
        switch (pdto.getCategory()){
            case "hotel" :
                prolist =  pDao.selectHBookList(pdto);
                url = "HBookDetail";
                break;
            case "air" :
                prolist =  pDao.selectABookList(pdto);

                url = "ABookDetail";
                break;
            case "rent" :
                prolist =  pDao.selectRBookList(pdto);
                url = "RBookDetail";
                break;
            case "leisure" :
                prolist =  pDao.selectLBookList(pdto);
                url = "LBookDetail";
                break;
        }
        mv.addObject("url", url);
        mv.addObject("prolist", prolist);

        mv.setViewName("ProBookList");

        //페이징 처리
        pdto.setPageNum(num);//원래 페이지 번호로 환원
        String pageHtml = getproPaging(pdto);
        mv.addObject("paging", pageHtml);

        session.setAttribute("pageNum", num);

        mv.setViewName("ProBookList");
        return mv;
    }

    private String getproPaging(PageDto pdto) {
        String pageHtml = null;

        if(pdto.getCategory() == null){
            pdto.setCategory("hotel");
        }

        int maxNum = 0;
        String listName = "";
        switch (pdto.getCategory()){
            case "hotel" :
                maxNum =  pDao.countHBookList(pdto);
                listName = "ProBookList?category=hotel&";
                break;
            case "air" :
                maxNum =  pDao.countABookList(pdto);
                listName = "ProBookList?category=air&";
                break;
            case "rent" :
                maxNum =  pDao.countRBookList(pdto);
                listName = "ProBookList?category=rent&";
                break;
            case "leisure" :
                maxNum =  pDao.countLBookList(pdto);
                listName = "ProBookList?category=leisure&";
                break;
        }

        //페이지에 보여질 번호 개수
        int pageCnt = 5;

        PagingUtil paging = new PagingUtil(
                maxNum,
                pdto.getPageNum(),
                pdto.getListCnt(),
                pageCnt,
                listName
        );

        pageHtml = paging.makePaging();

        return pageHtml;
    }

}


