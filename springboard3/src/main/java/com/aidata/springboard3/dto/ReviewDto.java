package com.aidata.springboard3.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ReviewDto {
    private int rno;
    private String rtitle;
    private String rcontents;
    private String cid;
    private Timestamp rdate;
    private int rviews;
    private String cname;
}
