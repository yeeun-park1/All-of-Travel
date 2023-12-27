package com.aidata.springboard3.controller;

import com.aidata.springboard3.dto.*;
import com.aidata.springboard3.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class ReviewController {
    @Autowired
    private BoardService bserv;

    @GetMapping ("reviewList")
    public ModelAndView reviewList(SearchDto sdto, HttpSession session){
        log.info("reviewList()");
        ModelAndView mv = bserv.reviewList(sdto, session);
        return mv;
    }

    @GetMapping ("reviewWrite")
    public String reviewWrite(){
        log.info("reviewWrite()");

        return "reviewWrite";
    }

    @PostMapping("writeProc")
    public String writeProc(@RequestPart List<MultipartFile> files, ReviewDto rdto, HttpSession session, RedirectAttributes rttr){
        log.info("writeProc()");

        String view = bserv.reviewWrite(files, rdto, session, rttr);
        return view;
    }
    @GetMapping("reviewDetail")
    public ModelAndView reviewDetail(int rno, HttpSession session, ReviewDto rdto){
        log.info("reviewDetail()");
        ModelAndView mv = bserv.getReview(rno, session, rdto);
        return mv;
    }

    @GetMapping("download")
    public ResponseEntity<Resource> fileDownload (HttpSession session, ReviewFileDto fdto) throws IOException {
        log.info("fileDownload()");
        ResponseEntity<Resource> resp =bserv.fileDownload(fdto, session);
        return resp;
    }
    @GetMapping("reviewDelete")
    public String reviewDelete (HttpSession session, RedirectAttributes rttr, int rno){
        log.info("reviewDelete()");

        String view = bserv.reviewDelete(session, rttr, rno);

        return view;
    }

    @GetMapping("reviewUpdateForm")
    public ModelAndView reviewUpdateForm(int rno) {
        log.info("reviewUpdateForm()");
        ModelAndView mv = bserv.reviewUpdate(rno);
        return mv;
    }

    @PostMapping("updateProc")
    public String updateProc(List<MultipartFile> files,
                             ReviewDto review,
                             HttpSession session,
                             RedirectAttributes rttr){
        log.info("updateProc()");
        String view = bserv.reviewUpdate(files, review, session, rttr);
        return view;
    }
}
