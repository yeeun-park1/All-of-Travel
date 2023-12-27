package com.aidata.springboard3.controller;

import com.aidata.springboard3.dto.CommentDto;
import com.aidata.springboard3.dto.ReviewDto;
import com.aidata.springboard3.dto.ReviewFileDto;
import com.aidata.springboard3.service.BoardService;
import com.aidata.springboard3.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@Slf4j
public class CustomerRestController {
    @Autowired
    private CustomerService cserv;
    @Autowired
    private BoardService bserv;

    @GetMapping("idcheck")
    public String idcheck(String cid){
        log.info("idcheck()");
        String result = cserv.idcheck(cid);
        return result;
    }

    @PostMapping("delFile")
    public List<ReviewFileDto> delFile(ReviewFileDto rFile,
                                       HttpSession session){
        log.info("delFile()");
        List<ReviewFileDto> rfList = bserv.delFile(rFile, session);

        return rfList;
    }

    @PostMapping("replyInsert")
    public CommentDto replyInsert(CommentDto comment){
        log.info("replyInsert()");
        comment = bserv.commentInsert(comment);
        return comment;
    }

}
