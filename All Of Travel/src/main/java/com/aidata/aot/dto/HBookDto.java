package com.aidata.aot.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class HBookDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sdate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate edate;
    private int hnum;
    private int hbnum;
    private int roomnum;
    private String mid;
    private String hname;
    private String horiname;
    private String stime;
    private String etime;
    private String rname;
    private int hprice;
    private int count;
    private int totalPrice;
    private int totalDate;
    private String category;
    private String status;
    private String paymentMethod;
}
