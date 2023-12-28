package com.aidata.aot.dto;

import lombok.Data;

@Data
public class LBookDto {
    private int lbnum;
    private int tnum;
    private int lnum;
    private String mid;
    private String lname;
    private String ltel;
    private String laddr;
    private String llogo;
    private String tname;
    private String tdate;
    private String tinfo;
    private int tcount;
    private int ttotalprice;
    private int totalprice; //계산용
    private String category;
    private String status;
    private String paymentMethod;
}
