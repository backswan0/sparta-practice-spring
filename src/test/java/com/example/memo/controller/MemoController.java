package com.example.memo.controller;

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestController
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();
    // key 값 = Long

    public MemoResponseDto createMemo (@RequestBody MemoRequestDto dto) {
     /*
     [해야 할 일]
     [1] 식별자가 1씩 증가해야 함. 중복이 되면 안 되니까
     [2] 요청받은 데이터로 Memo를 생성해야 함
     [3] Inmemory DB에 메모 저장 == 임의로 데이터베이스를 사용하는 게 아니라, Java의 Map 자료구조를 이용하는 것
         == 자료구조라서 프로젝트를 실행했다가 종료하면 그 안의 모든 데이터가 지워질 거라서 Inmemory라고 표현
      */

        return new MemoResponseDto();
         // memoList.isEmpty() ? 1 : Collections.max(memoList.keySet())
    }
}
