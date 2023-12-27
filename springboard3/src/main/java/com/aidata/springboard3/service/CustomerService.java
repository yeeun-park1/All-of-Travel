package com.aidata.springboard3.service;

import com.aidata.springboard3.dao.CustomerDao;
import com.aidata.springboard3.dao.ReivewDao;
import com.aidata.springboard3.dto.CustomerDto;
import com.aidata.springboard3.dto.ReviewDto;
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

import java.io.File;
import java.util.List;

@Slf4j
@Service
public class CustomerService {
    @Autowired
    private CustomerDao cdao;
    @Autowired
    private ReivewDao rdao;
    @Autowired
    private BoardService bserv;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;

    private BCryptPasswordEncoder pEncoder = new BCryptPasswordEncoder();

    public String idcheck(String cid) {
        log.info("idcheck()");

        String result = null;
        int ccnt = cdao.selectcid(cid);
        if (ccnt == 0) {
            result = "ok";
        } else {
            result = "fail";
        }
        return result;
    }

    public String joinProc(CustomerDto customer, RedirectAttributes rttr) {
        log.info("joinProc()");

        String view = null;
        String msg = null;

        String enpwd = pEncoder.encode(customer.getCpassword());
        customer.setCpassword(enpwd);
        try {
            cdao.insertCustomer(customer);
            view = "redirect:/";
            msg = "가입성공";
        } catch (Exception e) {
            e.printStackTrace();
            view = "redirect:joinForm";
            msg = "가입실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public String loginProc(CustomerDto customer, RedirectAttributes rttr, HttpSession session) {
        log.info("loginProc()");
        String view = null;
        String msg = null;

        String enpwd = cdao.selectPwd(customer.getCid());
        if (enpwd != null) {
            if (pEncoder.matches(customer.getCpassword(), enpwd)) {
                customer = cdao.selectCustomer(customer.getCid());
                session.setAttribute("customer", customer);
                view = "redirect:/";
                msg = "로그인 성공!";
            } else {
                view = "redirect:LoginForm";
                msg = "비밀번호가 틀렸습니다!";
            }
        } else {
            view = "redirect:LoginForm";
            msg = "아이디가 틀렸습니다.";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

        public String deleteMember(HttpSession session, RedirectAttributes rttr, ReviewDto rdto, String cid) {
        String view=null;
        String msg = null;

        TransactionStatus status = manager.getTransaction(definition);

        CustomerDto member = (CustomerDto) session.getAttribute("customer");
        if(member.getCid() == cid) {
            try {
                List<String> fileNameList = rdao.selectFileName(rdto.getRno());
                // 파일 목록 삭제
                rdao.deleteFiles(rdto.getRno());

                // 댓글 삭제
                rdao.deleteReply(rdto.getRno());

                //리뷰 삭제
                rdao.deleteReview(rdto.getRno());

                if (fileNameList.size() != 0) {
                    deleteFiles(fileNameList, session);
                }

                cdao.deleteMember(member.getCid());
                manager.commit(status);

                view = "redirect:reviewList?pageNum=1";
                msg = "삭제 성공♥";

            } catch (Exception e) {
                e.printStackTrace();
                manager.rollback(status);
                view = "redirect:reviewDetail?rno=" + rdto.getRno();
                msg = "삭제 실패ㅠㅠ";
            }
        }
            rttr.addFlashAttribute("msg", msg);
            return view;
    }
    private void deleteFiles(List<String> fileNameList, HttpSession session) throws Exception{
        log.info("deleteFiles()");
        log.info("deleteFiles()");
        //파일 위치
        String realPath = session.getServletContext()
                .getRealPath("/");
        realPath += "upload/";

        for(String sn : fileNameList){
            File file = new File(realPath + sn);
            if(file.exists() == true){//파일 존재 확인 후
                file.delete();//파일 삭제
            }
        }
    }

}//class end
