package com.aidata.aot.dto;

import lombok.Data;

@Data
public class PageDto {
    private int listCnt=5;
    private int totalPage;
    private int pageNum=1;
    private int company;
    private int book;
    private String colname;
    private String keyword;
    private int maxNum;
    private String category;
    private String mid;
    private String goods;
    private String logo;
    private String status;
    private String cname;
}
