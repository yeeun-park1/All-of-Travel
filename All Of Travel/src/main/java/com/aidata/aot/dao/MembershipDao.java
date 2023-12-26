package com.aidata.aot.dao;

import com.aidata.aot.dto.BookmarkDto;
import com.aidata.aot.dto.MembershipDto;
import com.aidata.aot.dto.PageDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MembershipDao {
    //bookmark
    String selectHlogo(int company);

    int selectCount(PageDto pdto);

    void insertBookmark(BookmarkDto bdto);

    void deleteBookmark(BookmarkDto bdto);

    //evaluate
    void updateMpoint(MembershipDto member);

    //login
    int selectId(String mid);

    void insertMember(MembershipDto member);

    String selectPassword(String mid);

    MembershipDto selectMember(String mid);

    void updateMemberPoint(MembershipDto member);

    List<BookmarkDto> selectBookmarkList(PageDto pdto);

    String selectAlogo(int company);

    String selectRlogo(int company);

    String selectLlogo(int company);

    int selectListCount(PageDto pdto);

    String selectCategory(int knum);

    List<Integer> selectKnum(String mid);

    void updatePassword(MembershipDto member);

    String selectFoundId(String mlname, String mfname, String mphone);

    void updateMember(MembershipDto member);
}
