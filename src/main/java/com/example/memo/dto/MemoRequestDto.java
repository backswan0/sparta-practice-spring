package com.example.memo.dto;

import lombok.Getter;

@Getter
public class MemoRequestDto {
    // id는 서버에서 관리하기 때문에 요청받지 않는다.
    private String title;
    private String contents;
}