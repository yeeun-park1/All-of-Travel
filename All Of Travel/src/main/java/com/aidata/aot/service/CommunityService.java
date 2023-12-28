package com.aidata.aot.service;

import com.aidata.aot.dao.CommunityDao;
import com.aidata.aot.dao.MembershipDao;
import com.aidata.aot.dto.*;
import com.aidata.aot.util.PagingUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.ui.Model;
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
public class CommunityService {
    @Autowired
    private CommunityDao cDao;
    @Autowired
    private MembershipDao mDao;
    @Autowired
    private PlatformTransactionManager manager;
    @Autowired
    private TransactionDefinition definition;


    public ModelAndView ReviewList(String category, HttpSession session, PageDto pdto) {
        log.info("ReviewList()");
        ModelAndView mv = new ModelAndView();
        int num = pdto.getPageNum();

        pdto.setPageNum((num-1)*pdto.getListCnt());
        pdto.setCategory(category);
        int count = cDao.selectReviewCount(pdto);
        mv.addObject("count", count);
        List<ReviewDto> reList = cDao.selectReview(pdto);
        mv.addObject("reList", reList);


        pdto.setPageNum(num);

        String paging = getPaging(pdto);
        mv.addObject("paging", paging);
        session.setAttribute("pageNum", num);
        session.setAttribute("pdto", pdto);

        mv.setViewName("ReviewList");
        return mv;
    }

    private String getPaging(PageDto pdto) {
        String paging =null;

        int maxNum = cDao.selectReviewCount(pdto);
        int pageCnt = 5;

        //url
        String listName = "ReviewList?category="+pdto.getCategory()+"&";

        if(pdto.getKeyword() != null){
            listName += "&keyword="+pdto.getKeyword();
        }
        PagingUtil pu = new PagingUtil(maxNum, pdto.getPageNum(), pdto.getListCnt(), pageCnt, listName);

        paging = pu.makePaging();

        return paging;
    }

    public ModelAndView getReview(int renum,HttpSession session, ReviewDto rdto) {
        log.info("getReview()");
        ModelAndView mv = new ModelAndView();

        MembershipDto member = (MembershipDto) session.getAttribute("member");
        //조회수 +1씩 증가
        ReviewDto rv = cDao.selectReview2(renum);

        //조회수 +1씩 증가
        int views = rv.getViews();
        String rmid = rv.getMid();
        if (member == null || !member.getMid().equals(rmid)) {
            views ++;
            rv.setViews(views);
            cDao.updateViewPoint(rv);
        }
        //조회수가 증가하게 세션에 저장
        rdto = cDao.selectReview2(renum);
        session.setAttribute("review", rv);
        mv.addObject("review", rv);

        if (member != null){
            String mid = member.getMid();
            mv.addObject("mid", mid);
        }
        //게시글 번호로 선택한 게시물 가져오기
        ReviewDto review = cDao.selectReview2(renum);
        mv.addObject("review", review);

        //게시글의 파일목록 가져오기
        List<ReviewFileDto> rfList = cDao.selectReviewFile(renum);
        mv.addObject("rfList", rfList);

        //게시글의 댓글목록 가져오기
        List<CommentDto> coList = cDao.selectReplyList(renum);
        mv.addObject("coList", coList);
        mv.setViewName("ReviewDetail");
        return mv;
    }
    public String reviewWrite(List<MultipartFile> files, ReviewDto reviewDto, HttpSession session, RedirectAttributes rttr, PageDto pageDto) {
        log.info("reviewWrite()");

        //트랜젝션 상태 처리 객체
        TransactionStatus status = manager.getTransaction(definition);

        String view = null;
        String msg = null;

        try {
            //글내용 저장.
            cDao.insertReview(reviewDto);

            fileUpload(files, session, reviewDto.getRenum());
//작성자 point 수정
            MembershipDto member = (MembershipDto) session.getAttribute("member");
            int point = member.getMpoint() + 10;
            if (point > 100) {//point가 100을 넘지 않도록 필터링.
                point = 100;
            }
            member.setMpoint(point);
            mDao.updateMemberPoint(member);
            //세션에 새 정보를 저장.
            member = mDao.selectMember(member.getMid());
            session.setAttribute("member", member);
            //세션에 같은 이름으로 set을 하면 덮어쓰기가 된다.

            session.setAttribute("category", pageDto.getCategory());

            manager.commit(status);//최종 승인
            view = "redirect:ReviewList?pageNum=1&category=" + pageDto.getCategory();
            msg = "작성 성공";
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);//취소
            view = "redirect:WriteForm";
            msg = "작성 실패";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;
    }

    private void fileUpload(List<MultipartFile> files, HttpSession session, int renum) throws Exception {
        log.info("fileUpload()");

        String realPath = session.getServletContext().getRealPath("/");
        log.info(realPath);
        realPath += "upload/";
        File folder = new File(realPath);
        if (folder.isDirectory() == false) {
            folder.mkdir();
        }
        for (MultipartFile mf : files) {
            String oriname = mf.getOriginalFilename();
            if (oriname.equals("")) {
                return;//업로드할 파일 없음. 파일 저장 작업 종료.
            }


            ReviewFileDto fd = new ReviewFileDto();
            fd.setRenum(renum);//게시글 번호 저장.
            fd.setForiname(oriname);//원래 파일명 저장.
            String sysname = System.currentTimeMillis()
                    + oriname.substring(oriname.lastIndexOf("."));
            //air.jpg -> 1212412413.jpg
            fd.setFsysname(sysname);

            //파일 저장(upload폴더에...)
            File file = new File(realPath + sysname);
            //......./.../.../webapp/upload/1212412413.jpg
            mf.transferTo(file);//하드디스크에 저장.

            //파일 정보 저장(DB에...)
            cDao.insertFile(fd);
        }

    }

    public ResponseEntity<Resource> fileDownload(ReviewFileDto fd, HttpSession session) throws IOException {
        log.info("fileDownload()");
        //파일 저장 경로 및 경로와 파일명을 조합
        String realPath = session.getServletContext().
                getRealPath("/");
        realPath += "upload/" + fd.getFsysname();

        //실제 파일이 저장된 하드디스크까지의 통로를 수립.
        InputStreamResource fResource =
                new InputStreamResource(
                        new FileInputStream(realPath));

        //파일명이 한글인 경우의 처리(UTF-8로 인코딩)
        String fileName = URLEncoder
                .encode(fd.getForiname(), "UTF-8");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .cacheControl(CacheControl.noCache())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + fileName)
                .body(fResource);
    }

    public ModelAndView updateReview(int renum,PageDto pageDto) {
        log.info("updateReview()");
        ModelAndView mv = new ModelAndView();
        //게시글 내용 가져오기
        ReviewDto reviewDto = cDao.getReview(renum);
        //파일목록 가져오기
        List<ReviewFileDto> fList = cDao.selectFileList(renum);
        //mv에 담기
        mv.addObject("reviewDto", reviewDto);//board
        mv.addObject("fList", fList);
        mv.addObject("pageDto", pageDto);
        //템플릿 지정.
        mv.setViewName("WriteUpdate");
        return mv;
    }

    public String updateReview(List<MultipartFile> files, ReviewDto reviewDto, HttpSession session, RedirectAttributes rttr, PageDto pageDto) {
        log.info("updateReview()");

        TransactionStatus status =
                manager.getTransaction(definition);

        String view = null;
        String msg = null;

        try {
            cDao.updateReview(reviewDto);
            fileUpload(files, session, reviewDto.getRenum());

            manager.commit(status);
//            view = "redirect:ReviewDetail?renum="
//                    + reviewDto.getRenum();
            session.setAttribute("category", pageDto.getCategory());
            view = "redirect:ReviewList?pageNum=1&category=" + pageDto.getCategory();


            msg = "수정 성공";
        } catch (Exception e) {
            e.printStackTrace();
            manager.rollback(status);
//            view = "redirect:WriteUpdate?renum="
//                    + reviewDto.getRenum();
//            msg = "수정 실패";
            view = "redirect:WriteUpdate?renum=" + reviewDto.getRenum() + "&category=" + pageDto.getCategory();
            msg = "수정 실패";
        }
        rttr.addFlashAttribute("msg", msg);
        return view;
    }
    public String deleteReview(int renum, HttpSession session, RedirectAttributes rttr, ReviewDto review,PageDto pageDto) {
        log.info("deleteReview()");

        //트랜잭션
        TransactionStatus status = manager.getTransaction(definition);

        String view = null;
        String msg = null;

        try {
            //0. 파일 삭제 목록 구하기
            List<String> fList = cDao.selectFnameList(renum);
            //파일 목록삭제
            cDao.deleteFiles(renum);
            // 댓글 목록삭제
            cDao.deleteComment(renum);
            // 게시글 삭제
            cDao.deleteReview(renum);

            //파일 삭제 처리
            if (fList.size() != 0) {
                deleteFiles(fList, session);
            }

            manager.commit(status);

            view = "redirect:ReviewList?pageNum=1&category=" + pageDto.getCategory();
            msg = "삭제 성공";
        } catch (Exception e) {
            e.printStackTrace();

            manager.rollback(status);

            view = "redirect:ReviewDetail?renum=" + renum;
            msg = "삭제 실패";
        }
        rttr.addFlashAttribute("msg", msg);

        return view;
    }
    private void deleteFiles(List<String> fList, HttpSession session) throws Exception {
        log.info("deleteFiles");
        //파일 위치
        String realPath = session.getServletContext().getRealPath("index");
        realPath += "upload/";

        for (String sn : fList) {
            File file = new File(realPath + sn);
            if (file.exists() == true) { //파일 존재 확인 후
                file.delete(); //파일 삭제
            }
        }
    }
    public List<ReviewFileDto> delFile(ReviewFileDto rfFile, HttpSession session) {
        log.info("delFile()");
        List<ReviewFileDto> fList = null;

        //파일 경로 설정
        String realPath = session.getServletContext().getRealPath("/");
        realPath += "upload/" + rfFile.getFsysname();

        try {
            //파일 삭제
            File file = new File(realPath);
            if (file.exists()) {
                if (file.delete()) {
                    //해당 파일 정보 삭제(DB)
                    cDao.deleteFile(rfFile.getFsysname());
                    //나머지 파일 목록 다시 가져오기
                    fList = cDao.selectReviewFile(rfFile.getRenum());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fList;
    }
    public CommentDto commentInsert(CommentDto comment) {
        log.info("commentInsert()");

        try {
            cDao.insertComment(comment);
            comment = cDao.selectLastComment(comment.getConum());
        } catch (Exception e) {
            e.printStackTrace();
            comment = null;
        }
        return comment;
    }

}
