package com.aidata.aot.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class aBookDto {
    private int abnum;
    private String mid;
    private int anum; //항공사 기본키
    private int fnum; //항공편 기본키
    private String aname; //항공사명
    private String fname; //항공편명
    private String sgrade;
    private String alogo;
    private String snumber;
    private String d_airport;
    private String a_airport;
     private String d_time;
     private String a_time;
    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate d_date; //대여 시작 날짜
    @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate a_date; //대여 종료 날짜
    private int abcount;
    private int totalprice;
    private String category;
    private String status;
    private int fprice;
}
