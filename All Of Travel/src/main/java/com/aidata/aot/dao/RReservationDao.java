package com.aidata.aot.dao;

import com.aidata.aot.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RReservationDao {
    //렌트회사정보
    List<RentDto> selectRentList(PageDto page);
    //각 렌트회사에 렌트카 리스트
    List<clistDto> selectCarList(int company);
    //렌트카 사진관련
    List<RentFileDto> selectFileList(int company);
    //하나의 차량정보 가져오기
    clistDto selectCarDetail(int cnum);
    //회사정보
    RentDto selectTheRent(int company);
    //해당 차량사진 가져오기
    String selectCarFile(int cnum);


    rbookDto selectCarInfo(int cnum);

    String selectPayment(rbookDto rbook);


    void insertRbook(rbookDto rbook);

    rbookDto selectRbook(int rbnum);
    //예약글 수정 메소드
    void updateRbook(rbookDto rbook);

    int selectCinsuprice(rbookDto rbook);

    void deleteRbook(int rbnum);
    

    String selectInsu(int cinsuprice);

    int selectTotaldate(int rbnum);

    int selectRprice(int rbnum);
}
