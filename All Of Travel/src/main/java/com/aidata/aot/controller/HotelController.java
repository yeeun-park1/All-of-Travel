package com.aidata.aot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HotelController {

    @GetMapping("index")
    public String index(){
        log.info("index()");
        return "index";
    }
    @GetMapping("HotelList")
    public String HotelList(){
        log.info("HotelList()");
        return "HotelList";
    }
}
