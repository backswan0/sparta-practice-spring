package com.example.memo.entity;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Memo {
    private Long id;
    private String title;
    private String contents;


    public void update(MemoRequestDto dto) {
        this.title = dto.getTitle();
        this.contents = dto.getTitle();
        /*
        매개변수인 MemoResponseDto dto == 요청 정보
         */
    }

    public void updateTitle(MemoRequestDto dto) {
        this.title = dto.getTitle();
    }
}