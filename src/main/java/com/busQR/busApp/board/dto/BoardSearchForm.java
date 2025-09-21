package com.busQR.busApp.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class BoardSearchForm {
    // 검색 대상 필드
    public enum Field { TITLE, CONTENT, TITLE_CONTENT, AUTHOR }

    private Field field = Field.TITLE_CONTENT; // 기본: 제목+내용
    private String query;                      // 검색어
    private String author;                     // 작성자 닉네임 검색
    private LocalDate fromDate;                // 작성 시작일 (선택)
    private LocalDate toDate;                  // 작성 종료일 (선택)

}
