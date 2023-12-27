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

    List<PageDto> selectHProList(PageDto pdto);

    List<PageDto> selectAProList(PageDto pdto);

    List<PageDto> selectRProList(PageDto pdto);

    List<PageDto> selectLProDList(PageDto pdto);

    int countHProList(PageDto pdto);

    int countAProList(PageDto pdto);

    int countRProList(PageDto pdto);

    int countLProDList(PageDto pdto);

    int countHPreList(PageDto pdto);

    int countAPreList(PageDto pdto);

    int countRPreList(PageDto pdto);

    int countLPreDList(PageDto pdto);

    List<PageDto> selectHPreList(PageDto pdto);

    List<PageDto> selectAPreList(PageDto pdto);

    List<PageDto> selectRPreList(PageDto pdto);

    List<PageDto> selectLPreDList(PageDto pdto);

}
