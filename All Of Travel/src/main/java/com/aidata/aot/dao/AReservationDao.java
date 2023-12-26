package com.aidata.aot.dao;

import com.aidata.aot.dto.AirLineDto;
import com.aidata.aot.dto.FlightDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.aBookDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AReservationDao {

    List<AirLineDto> selectAirLine(PageDto page);

    List<FlightDto> selectFlightList(int company);


    FlightDto selectFlight(int fnum);

    AirLineDto selectTheAirLine(int company);

    void insertAbook(aBookDto abook);

    aBookDto selectAbook(int abnum);

    void updateAbook(aBookDto abook);

    void deleteAbook(int abnum);


    Integer selectFprice(aBookDto fprice);

    int selectfnum(aBookDto abook);
}
