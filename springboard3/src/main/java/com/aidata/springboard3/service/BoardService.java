package com.aidata.springboard3.service;

import com.aidata.springboard3.dao.HospitalDao;
import com.aidata.springboard3.dao.ReivewDao;
import com.aidata.springboard3.dto.*;
import com.aidata.springboard3.util.PagingUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@Service
public class BoardService {

    @Autowired
    private HospitalDao hdao;
    @Autowired
    private ReivewDao rdao;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;

    private int lcnt=5;
    private int pnum=1;
    public ModelAndView HostpitalList(SearchDto sdto, HttpSession session, HospitalDto hdto){

        ModelAndView mv = new ModelAndView();

        int num=sdto.getPageNum();

        session.setAttribute("hospital", hdto);
        mv.addObject("hospital", hdto);

        if(sdto.getListCnt()==0){
            sdto.setListCnt(lcnt);
        }

        if(sdto.getPageNum()==0){
            sdto.setPageNum(pnum);
        }

        sdto.setPageNum((num-1) * sdto.getListCnt());


        List<HospitalDto> hList = hdao.selectHosptialList(sdto);

        mv.addObject("hList", hList);

        sdto.setPageNum(num);

        int maxNum = hdao.selectBoardcnt(sdto);

        String pageHtml = getPaging(sdto, "HosptialList?", maxNum);
        mv.addObject("paging", pageHtml);
        session.setAttribute("pageNum", num);

        if(sdto.getColname() != null){
            session.setAttribute("sdto", sdto);
        }else {
            session.removeAttribute("sdto");
        }
        mv.setViewName("HosptialList");
        //ModelandVie는 무조건 마지막에 setViewName해줘야한다
        return mv;
    }
    public ModelAndView reviewList(SearchDto sdto, HttpSession session) {
        log.info("reviewList()");
        ModelAndView mv = new ModelAndView();

        int num=sdto.getPageNum();

        if(sdto.getListCnt()==0){
            sdto.setListCnt(lcnt);
        }
        if(sdto.getPageNum()==0){
            sdto.setPageNum(pnum);
        }

        sdto.setPageNum((num-1) * sdto.getListCnt());

        List<ReviewDto> rList = rdao.selectReviewList(sdto);
        mv.addObject("rList", rList);
        sdto.setPageNum(num);
        int maxNum = rdao.selectReivewCnt(sdto);
        String pageHtml = getPaging(sdto,"reviewList?", maxNum);
        mv.addObject("paging", pageHtml);
        session.setAttribute("pageNum", num);

        if(sdto.getColname() != null){
            session.setAttribute("sdto", sdto);
        }else {
            session.removeAttribute("sdto");
        }
        mv.setViewName("reviewList");
        return mv;
    }
    private String getPaging(SearchDto sdto, String listName, int maxNum) {
        String pageHtml = null;
        //int maxNum = hdao.selectBoardcnt(sdto);
        int pageCnt = 5;
        if(sdto.getColname() != null){
            listName += "colname="+sdto.getColname()+"&keyword="+sdto.getKeyword()+"&";
        }
        PagingUtil paging = new PagingUtil(maxNum, sdto.getPageNum(), sdto.getListCnt(),pageCnt,listName);
        pageHtml = paging.makePaing();

        return pageHtml;
    }
    public String reviewWrite(List<MultipartFile> files, ReviewDto rdto, HttpSession session, RedirectAttributes rttr) {
        log.info("reviewWrite()");

        String view= null;
        String msg = null;

        TransactionStatus status = manager.getTransaction(definition);

        try{
            log.info("게시글번호 :" + rdto.getRno());
            rdao.insertReview(rdto);
            log.info("게시글번호 :" + rdto.getRno());

            fileUpload(files, session, rdto.getRno());

            CustomerDto customer = (CustomerDto) session.getAttribute("customer");
            log.info("customer()");
            int cpoint = customer.getCpoint()+1;

            if(cpoint>1000){
                cpoint = 1000;
            }

            customer.setCpoint(cpoint);
            session.setAttribute("customer", customer);

            manager.commit(status);
            view="redirect:reviewList?PageNum=1";
            msg="리뷰 작성 성공 ♥";
        }catch (Exception e){
            manager.rollback(status);
            view="redirect:reviewWrite";
            msg="리뷰 작성 실패 ㅠㅠ";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }

    public void fileUpload(List<MultipartFile> files, HttpSession session, int rno) throws Exception{
        log.info("fileUpload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info("realPath : " + realPath);
        realPath += "upload/";

        File folder = new File(realPath);
        if(folder.isDirectory()==false){
            folder.mkdir();
        }

        for(MultipartFile mf : files){
            String oriname = mf.getOriginalFilename();
            if(oriname.equals("")){
                return;
            }
            ReviewFileDto fdto = new ReviewFileDto();
            log.info("fdto()");
            fdto.setRf_rno(rno);
            fdto.setRf_oriname(oriname);

            String sysname = System.currentTimeMillis()+oriname.substring(oriname.lastIndexOf("."));
            fdto.setRf_sysname(sysname);

            File file = new File(realPath+sysname);

            mf.transferTo(file);
            rdao.insertFile(fdto);
        }
    }
    public ModelAndView getReview(int rno, HttpSession session, ReviewDto rdto) {
        log.info("getReview()");
        ModelAndView mv = new ModelAndView();

        CustomerDto customer = (CustomerDto) session.getAttribute("customer");
        if(customer != null) {
            mv.addObject("customer", customer);
        }
        int views = rdto.getRviews();
        String rcid = rdto.getCid();
        if(customer == null){
            views++;
            rdto.setRviews(views);
            rdao.updateViews(rdto);
        }else if(!customer.getCid().equals(rcid)){
            views++;
            rdto.setRviews(views);
            rdao.updateViews(rdto);
        }
        rdto = rdao.selectReview(rno);
        session.setAttribute("review", rdto);
        mv.addObject("review", rdto);

        List<ReviewFileDto> rfList = rdao.selectFileList(rno);
        log.info("rflist = " + rfList);

        mv.addObject("rfList", rfList);

        List<CommentDto> coList = rdao.selectReplyList(rno);
        mv.addObject("coList", coList);

        mv.setViewName("reviewDetail");

        return mv;
    }

    public ResponseEntity<Resource> fileDownload(ReviewFileDto fdto, HttpSession session) throws IOException {
        log.info("fileDownload()");

        String realPath=session.getServletContext().getRealPath("/");
        realPath += "upload/" + fdto.getRf_sysname();

        InputStreamResource fresource = new InputStreamResource(new FileInputStream(realPath));

        String fileName = URLEncoder.encode(fdto.getRf_oriname(),"UTF-8");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename="+fileName)
                .body(fresource);
    }

    public String reviewDelete(HttpSession session, RedirectAttributes rttr, int rno) {
        log.info("reviewDelete()");
        String view=null;
        String msg = null;

        TransactionStatus status = manager.getTransaction(definition);

        try{
            List<String> fileNameList = rdao.selectFileName(rno);

            // 파일 목록 삭제
            rdao.deleteFiles(rno);

            // 댓글 삭제
            rdao.deleteReply(rno);

            //리뷰 삭제
            rdao.deleteReview(rno);

            if(fileNameList.size() != 0) {
                deleteFiles(fileNameList, session);
            }

            manager.commit(status);

            view = "redirect:reviewList?pageNum=1";
            msg = "삭제 성공♥";

        }catch (Exception e){
            e.printStackTrace();
            manager.rollback(status);
            view="redirect:reviewDetail?rno="+rno;
            msg="삭제 실패ㅠㅠ";
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

    public List<ReviewFileDto> delFile(ReviewFileDto rFile, HttpSession session) {
        log.info("delFile()");
        List<ReviewFileDto> bfList = null;

        //파일 경로 설정.
        String realPath = session.getServletContext()
                .getRealPath("/");
        realPath += "upload/" + rFile.getRf_sysname();

        try {
            //파일 삭제
            File file = new File(realPath);
            if(file.exists()){
                if(file.delete()){
                    //해당 파일 정보 삭제(DB)
                    rdao.deleteFile(rFile.getRf_sysname());
                    //나머지 파일 목록 다시 가져오기
                    bfList = rdao.selectFileList(rFile.getRf_rno());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return bfList;

    }


    public CommentDto commentInsert(CommentDto comment) {
        log.info("commentInsert()");


        try {
            rdao.insertcomment(comment);
            comment = rdao.selectLastcomment(comment.getRc_no());
        }catch (Exception e){
            e.printStackTrace();
            comment = null;
        }
        return comment;
    }

    public ModelAndView reviewUpdate(int rno) {
        log.info("reviewUpdate()");
        ModelAndView mv = new ModelAndView();
        //게시글 내용 가져오기
        ReviewDto review = rdao.selectReview(rno);
        //파일목록 가져오기
        List<ReviewFileDto> rfList = rdao.selectFileList(rno);
        //mv에 담기
        mv.addObject("review", review);
        mv.addObject("rfList", rfList);
        //템플릿 지정.
        mv.setViewName("reviewUpdateForm");
        return mv;


    }

    public String reviewUpdate(List<MultipartFile> files, ReviewDto review, HttpSession session, RedirectAttributes rttr) {
        log.info("reviewUpdate()");

        TransactionStatus status =
                manager.getTransaction(definition);

        String view = null;
        String msg = null;

        try{
            rdao.updateReview(review);
            fileUpload(files, session, review.getRno());

            manager.commit(status);
            view = "redirect:reviewDetail?rno=" + review.getRno();
            msg = "수정 성공";
        } catch (Exception e){
            e.printStackTrace();
            manager.rollback(status);
            view = "redirect:reivewUpdateForm?rno=" + review.getRno();
            msg = "수정 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }
}
