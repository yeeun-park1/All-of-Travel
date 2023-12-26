package com.aidata.aot.dto;

import lombok.Data;

@Data
public class RentDto {
    private int rnum;
    private String rname;
    private String raddr;
    private String rtel;
    private String rpolicy;
    private String rlogo;
    private float rlati;
    private float rlong;
    private String category;
    private float rentavg;
}
