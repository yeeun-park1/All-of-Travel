package com.aidata.aot.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Data
public class FlightDto {

private int fnum;
private int anum;
private String aname;
private String alogo;
private String aterminal;
private String category;
private String fname;
private String d_airport;
private String a_airport;

    private String d_time;

    private String a_time;
private String d_date;
private String a_date;
private String finfo;
private String fpolicy;
private int fprice;
}
