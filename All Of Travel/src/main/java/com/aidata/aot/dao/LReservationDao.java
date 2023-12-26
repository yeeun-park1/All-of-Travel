package com.aidata.aot.dao;

import com.aidata.aot.dto.LBookDto;
import com.aidata.aot.dto.LeisureFileDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.TlistDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LReservationDao {

    List<TlistDto> selectLeisureList(PageDto pdto);

    List<TlistDto> selectTicket(int company);

    List<LeisureFileDto> selectFile(int company);
    
    TlistDto selectinfo(int company);

    String  selecttname(int company);

    TlistDto selectTicketinfo(int tnum);

    void insertlbook(LBookDto lbook);

    LBookDto getLBookDetail(int book);

    List<TlistDto> getTicket(int lnum);

    void changeLBook(LBookDto lbook);

    void deleteLBook(int lbnum);
}
