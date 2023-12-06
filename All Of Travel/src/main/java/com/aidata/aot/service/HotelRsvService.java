package com.aidata.aot.service;

import com.aidata.aot.dao.ReservationDao;
import com.aidata.aot.dto.HotelDto;
import com.aidata.aot.dto.PageDto;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Service
@Slf4j
public class HotelRsvService {
    @Autowired
    ReservationDao rdao;
    public ModelAndView HotelList(HttpSession session, PageDto page) {
        log.info("HotelList()");

        ModelAndView mv = new ModelAndView();

        //DAO로 호텔 리스트 가져와서 mv에 담기
        List<HotelDto> hlist = rdao.selectHotel(page);
        mv.addObject("hlist", hlist);

        mv.setViewName("HotelList");
        return mv;
    }
}
