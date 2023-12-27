package com.aidata.aot.dao;

import com.aidata.aot.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommunityDao {

    List<ReviewDto> selectReview(PageDto pdto);

    int selectReviewCount(PageDto pdto);

    void insertReview(ReviewDto reviewDto);
    //리뷰저장메소드

    void insertFile(ReviewFileDto fd);
    //파일정보저장메소드

    ReviewDto getReview(int renum);
    //리뷰글 하나만 가져오는 메소드

    List<ReviewFileDto> selectFileList(int renum);

    void updateReview(ReviewDto reviewDto);
    //리뷰글 번호에 해당하는 파일목록 가져오는 메소드

    List<ReviewFileDto> selectReviewFile(int renum);
    //파일의 저장 이름 목록을 구하는 메소드
    List<String> selectFnameList(int renum);
    //수정 시 단독 파일 삭제 메소드
    void deleteFile(String fsysname);
    //게시글 번호에 해당하는 파일목록 삭제 메소드
    void deleteFiles(int renum);
    //게시글 번호에 해당하는 댓글목록 삭제 메소드
    void deleteComment(int renum);
    //게시글 번호에 해당하는 게시글 삭제 메소드
    void deleteReview(int renum);
    //게시글 수정 메소드

    void insertComment(CommentDto comment);
    //저장 시 생성된 댓글 번호로 댓글정보 가져오는 메소드
    CommentDto selectLastComment(int conum);

    List<CommentDto> selectReplyList(int renum);

    List<ReviewDto> selectRreview(String category);


    ReviewDto selectReview2(int renum);

    void updateViewPoint(ReviewDto rdto);
}
