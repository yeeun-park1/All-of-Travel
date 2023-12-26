package com.aidata.aot.dao;

import com.aidata.aot.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HReservationDao {
    List<HotelDto> selectHotel(PageDto page);

    List<RoomDto> selectRoomList(int company);

    List<HotelFileDto> selectHFileList(int company);

    RoomDto selectRoom(int roomnum);

    HotelDto selectTheHotel(int company);

    void insertHbook(HBookDto hbook);

    HBookDto selectHbook(int book);

    void deleteHbook(int hbnum);

    Integer selectRprice(HBookDto hbook);

    int selectHmax(HBookDto hbook);

    void updateHbook(HBookDto hbook);

    int selectRoomnum(HBookDto hbook);

    int selectFhmax(int roomnum);
}
