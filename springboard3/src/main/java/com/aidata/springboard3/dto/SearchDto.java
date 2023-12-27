package com.aidata.springboard3.dto;

import lombok.Data;

@Data
public class SearchDto {
    private String colname;
    private String keyword;
    private int pageNum;
    private int listCnt;

}
