package com.aidata.aot.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CommentDto {
    private int conum;
    private int renum;
    private String mid;
    private Timestamp codate;
    private String contents;
}
