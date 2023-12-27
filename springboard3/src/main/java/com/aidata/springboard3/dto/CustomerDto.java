package com.aidata.springboard3.dto;

import lombok.Data;

@Data
public class CustomerDto {
    private String cid;
    private String cname;
    private String csex;
    private String cbirth;
    private String cemail;
    private String cnick;
    private String cpassword;
    private int cpoint;
    private String clname;
}