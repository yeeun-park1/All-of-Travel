package com.aidata.springboard3.dao;

import com.aidata.springboard3.dto.HospitalDto;
import com.aidata.springboard3.dto.SearchDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HospitalDao {
    List<HospitalDto> selectHosptialList(SearchDto sdto);

    int selectBoardcnt(SearchDto sdto);

    HospitalDto selectHospital(int hno);
}
