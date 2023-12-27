package com.aidata.springboard3.dao;

import com.aidata.springboard3.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReivewDao {

    List<ReviewDto> selectReviewList(SearchDto sdto);

    int selectReivewCnt(SearchDto sdto);

    void insertReview(ReviewDto rdto);

    void insertFile(ReviewFileDto fdto);

    void updateViews(ReviewDto review);

    ReviewDto selectReview(int rno);

    List<ReviewFileDto> selectFileList(int rno);

    List<CommentDto> selectReplyList(int rno);

    List<String> selectFileName(int rno);

    void deleteFiles(int rno);

    void deleteReply(int rno);

    void deleteReview(int rno);

    void insertcomment(CommentDto comment);

    CommentDto selectLastcomment(int rc_no);

    void deleteFile(String rfSysname);

    void updateReview(ReviewDto review);

}
