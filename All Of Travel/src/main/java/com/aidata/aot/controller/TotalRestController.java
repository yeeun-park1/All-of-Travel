package com.aidata.aot.controller;

import com.aidata.aot.dto.*;
import com.aidata.aot.service.CommunityService;
import com.aidata.aot.service.HotelRsvService;
import com.aidata.aot.service.MembershipService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class TotalRestController {
    @Autowired
    private MembershipService mServ;
    @Autowired
    private HotelRsvService hServ;
    @Autowired
    private CommunityService cServ;


    @PostMapping("bookmarkData")
    public String bookmarkData(BookmarkDto kdto, HttpSession session, PageDto pdto){
        log.info("bookmarkData()");
        String result = mServ.bookmarkData(kdto, session, pdto);
        return result;
    }
    @GetMapping("idCheck")
    public String idCheck(String mid){
        log.info("idCheck()");
        String result = mServ.idCheck(mid);
        return result;
    }

    @PostMapping("roomChanged")
    public Map<String, Integer> roomChanged(HBookDto hbook){
        log.info("roomChanged()");
        Map<String, Integer> hinfo = hServ.roomChanged(hbook);
        return hinfo;
    }

    @PostMapping("delFile")
    public List<ReviewFileDto> delFile(ReviewFileDto rfFile, HttpSession session){
        log.info("delFile()");
        List<ReviewFileDto> fList = cServ.delFile(rfFile, session);
        return fList;
    }

    @PostMapping("commentInsert")
    public CommentDto commentInsert(CommentDto comment){
        log.info("commentInsert()");
        comment = cServ.commentInsert(comment);
        return comment;
    }
}
