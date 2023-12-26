package com.aidata.aot.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class ReviewDto {
    private int renum;
    private String mid;
    private String title;
    private String contents;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate redate;
    private int views;
    private String category;
    private int company;
    private int maxnum;
    private String url;
}
