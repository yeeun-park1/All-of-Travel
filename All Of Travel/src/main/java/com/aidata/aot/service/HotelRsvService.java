package com.aidata.aot.service;

import com.aidata.aot.dao.HReservationDao;
import com.aidata.aot.dto.HotelDto;
import com.aidata.aot.dto.HotelFileDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.RoomDto;
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
import org.springframework.web.servlet.ModelAndView;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Service
@Slf4j
public class HotelRsvService {
    @Autowired
    HReservationDao rdao;
    public ModelAndView HotelList(PageDto page) {
        log.info("HotelList()");

        ModelAndView mv = new ModelAndView();

        //DAO로 호텔 리스트 가져와서 mv에 담기
        List<HotelDto> hlist = rdao.selectHotel(page);
        mv.addObject("hlist", hlist);

        mv.setViewName("HotelList");
        return mv;
    }

    public ModelAndView HotelDetail(int hnum) {
        log.info("HotelDetail()");

        ModelAndView mv = new ModelAndView();

        String hname = rdao.selectHname(hnum);
        mv.addObject("hname", hname);

        //DAO로 룸 리스트 가져와서 mv에 담기
        List<RoomDto> rlist = rdao.selectRoomList(hnum);
        mv.addObject("rlist", rlist);

        //DAO로 룸 파일 가져오기
        List<HotelFileDto> hfList = rdao.selectHFileList(hnum);
        mv.addObject("hfList", hfList);

        mv.setViewName("HotelDetail");
        return mv;
    }

}
