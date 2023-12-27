package com.aidata.springboard3.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDto {
    private int rc_no;
    private int rc_rno;
    private String rc_contents;
    private String rc_cid;
    private Timestamp rc_date;
}
