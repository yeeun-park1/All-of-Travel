package com.aidata.aot.dao;

import com.aidata.aot.dto.HotelDto;
import com.aidata.aot.dto.HotelFileDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.RoomDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HReservationDao {
    List<HotelDto> selectHotel(PageDto page);

    List<RoomDto> selectRoomList(int hnum);

    List<HotelFileDto> selectHFileList(int hnum);

    String selectHname(int hnum);
}
