package com.aidata.aot.service;

import com.aidata.aot.dao.MembershipDao;
import com.aidata.aot.dto.BookmarkDto;
import com.aidata.aot.dto.MembershipDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.util.PagingUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
@Slf4j
public class MembershipService {
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;
    @Autowired
    private MembershipDao mDao;
    //비밀번호 암호화 인코더
    private BCryptPasswordEncoder pEncoder =
            new BCryptPasswordEncoder();

    public String bookmarkData(BookmarkDto kdto, HttpSession session, PageDto pdto) {
        log.info("bookmarkData()");

        String result = null;

        TransactionStatus status = manager.getTransaction(definition);
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        String mid = member.getMid();
        kdto.setMid(mid);
        pdto.setCompany(kdto.getCompany());
        pdto.setCategory(kdto.getCategory());
        String category = kdto.getCategory();
        String url = null;
        // 중복체크
        int count = mDao.selectCount(pdto);
        if (count == 0) {
            switch (category) {
                case "hotel":
                    try {
                        String logo = mDao.selectHlogo(pdto.getCompany());
                        kdto.setOriname(logo);
                        log.info("북마크 번호 : " + kdto.getKnum());
                        url = "HotelDetail";
                        kdto.setUrl(url);
                        mDao.insertBookmark(kdto);
                        manager.commit(status);
                        log.info("북마크 번호 : " + kdto.getKnum());
                        result = "insert";
                    } catch (Exception e) {
                        e.printStackTrace();
                        manager.rollback(status);
                    }
                    break;
                case "air":
                    try {
                        String logo = mDao.selectAlogo(pdto.getCompany());
                        kdto.setOriname(logo);
                        url = "FlightList";
                        kdto.setUrl(url);
                        log.info("북마크 번호 : " + kdto.getKnum());
                        mDao.insertBookmark(kdto);
                        manager.commit(status);
                        log.info("북마크 번호 : " + kdto.getKnum());
                        result = "insert";
                    } catch (Exception e) {
                        e.printStackTrace();
                        manager.rollback(status);
                    }
                    break;
                case "rent":
                    try {
                        String logo = mDao.selectRlogo(pdto.getCompany());
                        kdto.setOriname(logo);
                        url = "RentDetail";
                        kdto.setUrl(url);
                        log.info("북마크 번호 : " + kdto.getKnum());
                        mDao.insertBookmark(kdto);
                        manager.commit(status);
                        log.info("북마크 번호 : " + kdto.getKnum());
                        result = "insert";
                    } catch (Exception e) {
                        e.printStackTrace();
                        manager.rollback(status);
                    }
                    break;
                case "leisure":
                    try {
                        String logo = mDao.selectLlogo(pdto.getCompany());
                        kdto.setOriname(logo);
                        url = "LeisureDetail";
                        kdto.setUrl(url);
                        log.info("북마크 번호 : " + kdto.getKnum());
                        mDao.insertBookmark(kdto);
                        manager.commit(status);
                        log.info("북마크 번호 : " + kdto.getKnum());
                        result = "insert";
                    } catch (Exception e) {
                        e.printStackTrace();
                        manager.rollback(status);
                    }
            }
        } else {
            try {
                mDao.deleteBookmark(kdto);
                manager.commit(status);
                log.info("북마크 번호 : " + kdto.getKnum());
                result = "delete";
            } catch (Exception e) {
                e.printStackTrace();
                manager.rollback(status);
            }
        }
        return result;
    }

    public String idCheck(String mid) {
        log.info("idCheck()");
        String result = null;

        int mcnt = mDao.selectId(mid);
        if (mcnt == 0) {
            result = "ok";
        } else {
            result = "fail";
        }

        return result;
    }

    public ModelAndView BookmarkList(HttpSession session, PageDto pdto) {
        log.info("BookmarkList()");

        ModelAndView mv = new ModelAndView();
        MembershipDto member = (MembershipDto) session.getAttribute("member");
        String mid = member.getMid();
        pdto.setMid(mid);

        int num = pdto.getPageNum();
        pdto.setPageNum((num-1)*pdto.getListCnt());

        List<BookmarkDto> klist = mDao.selectBookmarkList(pdto);
        int count = mDao.selectListCount(pdto);
        String url =  null;
            switch (pdto.getCategory()) {
                case "hotel":
                    url = "HotelDetail";
                    break;
                case "rent":
                    url = "RentDetail";
                    break;
                case "air":
                    url = "FlightList";
                    break;
                case "leisure":
                    url = "LeisureDetail";
                    break;
            }
        mv.addObject("klist", klist);
        mv.addObject("url", url);
        mv.addObject("count", count);
        pdto.setPageNum(num);

        String paging = getPaging(pdto);
        mv.addObject("paging", paging);

        mv.setViewName("BookmarkList");
        return mv;
    }

    private String getPaging(PageDto pdto ) {
        String paging =null;

        int maxNum =  mDao.selectListCount(pdto);
        int pageCnt = 5;
        String listName = "BookmarkList?mid=" + pdto.getMid() + "&category=" + pdto.getCategory() + "&";

        PagingUtil pu = new PagingUtil(maxNum, pdto.getPageNum(), pdto.getListCnt(), pageCnt, listName);

        paging = pu.makePaging();

        return paging;

    }


    public String memberJoin(MembershipDto member,
                             RedirectAttributes rttr) {
        log.info("memberJoin()");
        //가입 성공 시 첫페이지로, 실패 시 가입 페이지로 이동
        String view = null;
        String msg = null;//경고창으로 출력할 메시지 저장 변수

        //비밀번호 암호화 처리
        String encPwd = pEncoder.encode(member.getMpw());
        //암호화된 비밀번호를 다시 dto 객체에 저장
        member.setMpw(encPwd);

        try {
            mDao.insertMember(member);
            msg = "가입 성공";
            view = "redirect:index";//list 페이지 작성 시 변경할 예정.
        } catch (Exception e) {
            e.printStackTrace();
            msg = "가입 실패";
            view = "redirect:signup";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public String loginProc(MembershipDto member,
                            HttpSession session,
                            RedirectAttributes rttr) {
        log.info("loginProc()");
        String view = null;
        String msg = null;

        //DB에서 해당 id의 비밀번호(암호문) 가져오기.
        String encPwd = mDao.selectPassword(member.getMid());
        //encPwd에 암호화된 비밀번호가 들어가거나, - 해당 아이디가 존재
        //비밀번호가 들어오지 않거나(null). - 해당 아이디가 X
        if (encPwd != null) {
            //입력한 비번과 DB에서 가져온 비번 비교(matches)
            if (pEncoder.matches(member.getMpw(), encPwd)) {
                //로그인 성공.
                //회원 정보(아이디, 이름, 포인트, 등급이름) - from DB
                member = mDao.selectMember(member.getMid());
                //세션에 회원 정보 저장.
                session.setAttribute("member", member);
                //로그인 성공 후 게시판 목록 페이지로 이동.
                view = "redirect:index";
                msg = "로그인 성공";
            } else {
                //로그인 실패. - 비번을 잘못 입력한 경우
                view = "redirect:Login";
                msg = "비밀번호가 틀립니다.";
            }
        } else {
            //아이디가 없는 경우
            view = "redirect:Login";
            msg = "존재하지 않는 아이디입니다.";
        }
        //화면으로 메시지 보내기
        rttr.addFlashAttribute("msg", msg);

        return view;
    }


    public String logout(HttpSession session) {
        log.info("logout()");
        session.invalidate();
        return "redirect:index";
    }

    public String m_Update(MembershipDto member, HttpSession session, RedirectAttributes rttr) {

        String view = null;
        String msg = null;

        try {
            mDao.updateMember(member);
            session.setAttribute("member", member);
            msg = "정보수정 완료";
            view = "redirect:MyPage";
        } catch (Exception e){
            e.printStackTrace();
            msg = "정보수정 실패";
            view = "redirect:MypageUpdate";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;
    }
    public String findPwUpdateProc(MembershipDto member, HttpSession session, RedirectAttributes rttr) {
        log.info("findPwUpdateProc()");

        String view = null;
        String msg = null;

        try{
            String encpwd = pEncoder.encode(member.getMpw());
            member.setMpw(encpwd); //암호화문구로 덮기
            mDao.updatePassword(member);
            if(session.getAttribute("member") == null){
                msg = "변경 완료";
                view = "redirect:Login";
            } else {
                msg = "변경 완료";
                view = "redirect:MyPage";
            }

        } catch (Exception e){
            e.printStackTrace();
            msg = "변경 실패";
            view = "redirect:pwUpdate";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }
    public String mfindId(String mlname, String mfname, String mphone, HttpSession session, RedirectAttributes rttr) {
        log.info("mfindId");
        String view = null;
        String msg = null;

        String foundId = mDao.selectFoundId(mlname, mfname, mphone);
        if (foundId != null){
            session.setAttribute("mid", foundId);
            view = "redirect:findIdView";
            msg = "아이디를 조회하였습니다";
        }else {
            view = "redirect:findId";
            msg = "회원정보가 존재하지 않습니다.";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;

    }

}