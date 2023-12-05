package com.aidata.aot.dao;

import com.aidata.aot.dto.HotelDto;
import com.aidata.aot.dto.PageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReservationDao {
    List<HotelDto> selectHotel(PageDto page);
}
