package com.aidata.aot.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class RoomDto {
    private int hnum;
    private String hname;
    @JsonFormat(pattern = "HH:mm")
    private String hcit;
    @JsonFormat(pattern = "HH:mm")
    private String hcot;
    private String hent;
    private String hamenity;
    private String hpolicy;
    private String category;
    private Float hlati;
    private Float hlong;
    private int roomnum;
    private String rname;
    private int hprice;
    private int hmax;
    private String roriname;
    private String horiname;
}
