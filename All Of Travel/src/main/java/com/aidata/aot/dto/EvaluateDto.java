package com.aidata.aot.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
public class EvaluateDto {
    private int evnum;
    private String mid;
    private String category;
    private int company;
    private String title;
    private String review;
    private int score;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate evdate;
    private Float avg;
    private int count;
}
