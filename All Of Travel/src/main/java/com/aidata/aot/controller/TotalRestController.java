package com.aidata.aot.controller;

import com.aidata.aot.dto.BookmarkDto;
import com.aidata.aot.dto.HBookDto;
import com.aidata.aot.dto.PageDto;
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

import java.util.Map;

@RestController
@Slf4j
public class TotalRestController {
    @Autowired
    private MembershipService mServ;
    @Autowired
    private HotelRsvService hServ;

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
}
