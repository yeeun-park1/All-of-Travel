package com.aidata.aot.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class rbookDto {
    private int rbnum; //렌트예약번호
    private int rnum; //렌트회사 번호
    private int cnum; //렌트카 번호
    private String mid; //회원 아이디
    private String rname; //렌트카 회사 이름
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sdate; //대여 시작 날짜
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate edate; //대여 종료 날짜
    private String rlogo; //회사 로고
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime stime;
    private int rprice; //하루 대여가격
    private int cinsuprice; //차량보험 가격
    private String cbrand; //차량종류(이름)
    private int totalprice; //보험+차량+예약일 총 가격
    private int totaldate; //총 이용일수
    private String coriname; //차량사진
    private int totalinsu; //총 보험비 넘어가야하는거
    private String cinsu; //보험이름
    private String category;
}
