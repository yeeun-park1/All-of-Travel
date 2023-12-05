package com.aidata.aot.dto;

import lombok.Data;

@Data
public class PageDto {
    private int listCnt;
    private int totalPage;
    private int pageNum;
    private String colname;
    private String keyword;
    private int maxNum;
}
