package com.aidata.aot.dao;

import com.aidata.aot.dto.EvaluateDto;
import com.aidata.aot.dto.LBookDto;
import com.aidata.aot.dto.PageDto;
import com.aidata.aot.dto.TlistDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper

public interface PersonalBookingDao {

    //evaluate
    List<EvaluateDto> selectEval(PageDto pdto);

    List<Integer> selectScore(PageDto pdto);

    int selectCount(PageDto pdto);

    void updateHavg(EvaluateDto edto);

    void updateRavg(EvaluateDto edto);

    void updateAavg(EvaluateDto edto);

    void updateLavg(EvaluateDto edto);

    void insertEvaluate(EvaluateDto edto);

    List<PageDto> selectHBookList(PageDto pdto);

    List<PageDto> selectABookList(PageDto pdto);

    List<PageDto> selectRBookList(PageDto pdto);

    List<PageDto> selectLBookList(PageDto pdto);

    int countHBookList(PageDto pdto);

    int countABookList(PageDto pdto);

    int countRBookList(PageDto pdto);

    int countLBookList(PageDto pdto);


}
