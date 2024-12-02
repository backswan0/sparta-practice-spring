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

    public MemoResponseDto createMemo(@RequestBody MemoRequestDto dto) {
     /*
     [해야 할 일]
     [1] 식별자가 1씩 증가해야 함. 중복이 되면 안 되니까
     [2] 요청받은 데이터로 Memo를 생성해야 함
     [3] Inmemory DB에 메모 저장 == 임의로 데이터베이스를 사용하는 게 아니라, Java의 Map 자료구조를 이용하는 것
         == 자료구조라서 프로젝트를 실행했다가 종료하면 그 안의 모든 데이터가 지워질 거라서 Inmemory라고 표현
      */

        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;
        /*
        [1] 식별자가 1씩 증가하도록 삼항연산자를 사용해서 구현하기
        memoList.isEmpty(): 비어 있는지 검사
        1: 비어 있다면 1로 초기 값 설정
        Collections.max(memoList.keySet()): memoList.keySet() 안에 든 값 중 최댓값을 뽑아내는 메서드
        memoList.keySet(): memoList 안에 있는 key 값을 전부 뽑아내는 메서드
        == 모든 Long 값을 꺼내서 최댓값을 반환해주는 메서드
        우리는 1씩 증가해야 하므로 + 1 추가
        이후 추가: Long memoId =
         */

       Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());
        /*
        [2] 요청받은 데이터로 Memo 객체 생성하기
        MemoRequestDto 형태로 요청 받았기 때문에 Memo 객체로 바꿔줘야 한다.
        첫 번째 인자로 memoId 값 전달
        두 번째로 title과 세 번째로 contents를 전달해주어야 하는데, 두 데이터 모두 dto 안에 있으므로 getter 사용
        이후 Memo 타입 변수로 받아주기. 즉, 'Memo memo = ' 추가
         */

        memoList.put(memoId, memo);
        /*
        [3] 실제 데이터베이스 (현재는 자료구조) 안에 저장하기
        key 값은 memoId
        저장될 객체 형태는 memo
         */

        return new MemoResponseDto(memo);
        /*
        [4] 저장된 데이터를 MemoResponseDto 형태로 반환하기
        이후 MemoResponseDto 클래스로 돌아가서 생성자를 만들어준다.
        return new MemoResponseDto();
        그다음, 위와 같이 적은 곳의 () 안에 memo를 넣어준다.
         */
    }
}
