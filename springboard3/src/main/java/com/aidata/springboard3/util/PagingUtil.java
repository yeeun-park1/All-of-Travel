package com.aidata.springboard3.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class PagingUtil {
    private int maxNum; //전체 게시글 갯수
    private int pageNum; //현재 보이는 페이지의 번호
    private int listCnt; //페이지 당 보여질 게시글 갯수
    private int pageCnt; //보여질 페이지 번호의 갯수
    private String listName; //게시판이 여러개일 때
    // 처리해야되는 목록 이름

    public String makePaing() {
        String page = null;
        StringBuffer sb = new StringBuffer();

        //1. 전체페이지 수 구하기
        int totalPage = (maxNum % listCnt) > 0 ? maxNum / listCnt + 1 : maxNum / listCnt;
        if (totalPage == 0) {
            totalPage = 1;
        }
        //2. 현재 페이지가 속해있는 그룹번호 구하기
        //pageNum:시작번호 , pageCnt:한페이지당 보여줄 페이지 갯수
        int curGroup = (pageNum % pageCnt) > 0 ? pageNum / pageCnt + 1 : pageNum / pageCnt;

        //3. 현재 보이는 페이지 그룹의 시작 번호 구하기
        int start = (curGroup * pageCnt) - (pageCnt - 1);

        //4. 현재 보이는 페이지 그룹의 마지막 번호 구하기
        int end = (curGroup * pageCnt) >= totalPage ? totalPage : curGroup * pageCnt;

        //5. 이전 버튼 처리. 시작번호가 1일때는 생성 x
        if (start != 1) {
            sb.append("<a class='pno' href='/" + listName + "pageNum=" + (start - 1) + "'>◀</a>");
        }

        //6. 중간 페이지 번호 버튼 처리
        for (int i = start; i <= end; i++) {
            if (pageNum != i) { // 현재 페이지가 아닌 번호
                sb.append("<a class = 'pno' href = '/" + listName + "pageNum=" + i + "'>" + i + "</a>");
                /* 내가 만약 1페이지라면 숫자 1에는 링크가 필요없지만
                    다른 2 3 4 5 라는 한 그룹의 다른 숫자들에는 링크를
                    넣어줘야한다.
                 */
            } else { // 현재 페이지 (링크 x)
                sb.append("<font class = 'pno'>" + i + "</font>");
                // 그래서 현재페이지인 숫자에는 style만 주고 링크 안넣는다.
            }
        }

        //7. 다음 버튼 처리
        if (end != totalPage) {
            sb.append("<a class='pno' href='/" + listName + "pageNum=" + (end + 1) + "'>▶</a>");

        }
        // 8. StringBuffer -> String으로 변환.
        page = sb.toString();
        return page;
    }

}
