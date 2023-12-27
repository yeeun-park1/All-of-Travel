package com.aidata.aot.controller;

import com.aidata.aot.dto.MembershipDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.ReviewDto;
import com.aidata.aot.dto.ReviewFileDto;
import com.aidata.aot.service.CommunityService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class CommunityController {
    @Autowired
    private CommunityService cServ;

    @GetMapping("ReviewList")
    public ModelAndView ReviewList(String category, HttpSession session, PageDto pdto){
        log.info("ReviewList()");
        ModelAndView mv = cServ.ReviewList(category, session, pdto);
        return mv;
    }
    @GetMapping("ReviewDetail")
    public ModelAndView ReviewDetail(int renum, HttpSession session, ReviewDto rdto){
        log.info("ReviewDetail()", renum);
        ModelAndView mv = cServ.getReview(renum, session, rdto);
        return mv;
    }

    //리뷰게시글 삭제
    @GetMapping("ReviewDelete")
    public String ReviewDelete(int renum, HttpSession session, RedirectAttributes rttr, ReviewDto review,PageDto pageDto){
        log.info("ReviewDelete()");
        String view = cServ.deleteReview(renum, session, rttr,review,pageDto);
        return view;
    }

    @PostMapping("writeProc")
    public String writeProc(@RequestPart List<MultipartFile> files,
                            ReviewDto reviewDto,
                            HttpSession session,
                            RedirectAttributes rttr, PageDto pageDto){
        log.info("writeProc()");
        String view = cServ.reviewWrite(files, reviewDto, session, rttr,pageDto);
        return view;
    }
    @GetMapping("WriteForm")
    public String WriteForm(ReviewDto review, Model model){
        log.info("WriteForm()");
        model.addAttribute("review", review);
        return "WriteForm";
    }
    //파일 다운로드
    @GetMapping("download")
    public ResponseEntity<Resource> fileDownload (
            ReviewFileDto fd,
            HttpSession session) throws IOException {
        log.info("fileDownload()");
        ResponseEntity<Resource> resp =
                cServ.fileDownload(fd, session);
        return resp;
    }
    @GetMapping("WriteUpdate")
    public ModelAndView WriteUpdate(int renum, ReviewDto review, Model model,PageDto pageDto) {
        log.info("WriteUpdate()");
        ModelAndView mv = cServ.updateReview(renum,pageDto);
        return mv;
    }

    @PostMapping("updateProc")
    public String updateProc(List<MultipartFile> files,
                             ReviewDto reviewDto,
                             HttpSession session,
                             RedirectAttributes rttr, PageDto pageDto){
        log.info("updateProc()");
        String view = cServ.updateReview(files, reviewDto, session, rttr, pageDto);
        return view;
    }

}
