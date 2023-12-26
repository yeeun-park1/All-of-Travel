package com.aidata.aot.dto;

import lombok.Data;

@Data
public class clistDto {
    private int rnum; //렌트회사번호
    private String rname; //회사이름
    private String rpolicy; //차량대여정책
    private String rlogo; //회사로고사진
    private float rlati; //위도
    private float rlong; //경도
    private String category; //카테고리
    private int cnum; //렌트카 번호
    private String cbrand; //차종 겸 이름
    private int rprice; //차량가격
    private String cinfo; //차량정보
    private String csize; //차량사이즈
    private String cminage; //렌트가능 최소나이
    private String coriname; //차량사진
}
