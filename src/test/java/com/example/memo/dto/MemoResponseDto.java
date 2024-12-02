package com.example.memo.dto;

import com.example.memo.entity.Memo;
import lombok.Getter;

@Getter
public class MemoResponseDto {
    private Long id;
    private String title;
    private String contents;

    // MemoController 작성이 끝나면 생성자 만들기
    public MemoResponseDto (Memo memo) {
        this.id = memo.getId();
        this.title = memo.getTitle();
        this.contents = memo.getContents();
        /*
         memo 객체가 그대로 반횐되는 게 아니다.
         MemoResponseDto 형태로 바뀌어서 응답이 되어야 한다.
         */
    }


}
