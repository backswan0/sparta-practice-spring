package com.example.memo.controller;
/*
[절대 잊으면 안 되는 것]
첫째, src>test>java에 클래스를 넣지 않았는지 확인하자
     ➡️src>main>java이다. 그렇게 안 하고 postman을 실행하면 404 오류 뜬다.
둘째, import가 두 번 되지 않았는지 꼭 살펴보자.
 */

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/memos")
/*
[@RequestMapping]
- @PostMapping 사용 후 @RequestMapping을 추가하자.
- prefix하는 URL을 설정할 때 사용한다.
  == prefix처럼 하위 메서드 전체에 영향을 주기 때문에 'prefix'라고 표현함
- 공통적으로 들어가는 /memos라는 URL을 적어둔다.
 */

public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();
    // key 값 = Long

    // --------------------------- Create ---------------------------
    @PostMapping
    /*
    생성이기 때문에 @PostMapping을 사용한다.
    @PostMapping("/healthy") 이렇게 적으면?
    == localhost:8080/memos/healthy
    == 영향 범위는 메서드의 블럭만. 즉 다른 메서드에 영향을 주려면 해당 메서드 위에 다시 annotation을 써야 한다.
     */
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

    // --------------------------- Read ---------------------------
    @GetMapping("/{id}")
    // 이미 prefix로 /memos를 적었으므로 식별자를 추가로 적었다.
    public MemoResponseDto findMemoById(@PathVariable Long id) {
        // 식별자를 파라미터(매개변수)로 바인딩할 때는 @PathVariable을 사용한다.
        Memo memo = memoList.get(id);

        return new MemoResponseDto(memo);
    }

    // --------------------------- Update ---------------------------
    @PutMapping("/{id}")
    /*
    [1] 전체 수정이므로 Put이다.
    [2] 단건을 수정해야 하므로 아까처럼 경로변수가 필요하다.
    [3] id값 뿐만 아니라 우리가 어떤 데이터로 수정할지 그 데이터도 요청 받아야 한다.
        ➡️RequestDto 형태로 받기로 한 걸 기억하자!
        ➡️지금은 title과 contents 둘 다 수정할 수 있다고 가정했다.
        == @PathVariable Long id 옆에 @RequestBody MemoRequestDto dto를 작성한 이유
     */
    public MemoResponseDto updateMemoById(@PathVariable Long id, @RequestBody MemoRequestDto dto) {
        Memo memo = memoList.get(id);
        /*
        - 수정하려면 우선 저장된 메모를 꺼내와야 하므로 위와 같이 작성한다.
        - 이후 Memo 클래스로 돌아가서 update() 메서드를 새로 만들자.
         */

        memo.update(dto);
        return new MemoResponseDto(memo);
    }

    // --------------------------- Delete ---------------------------
    @DeleteMapping("/{id}")
    public void deleteMemo (@PathVariable Long id) {
        // 삭제라서 따로 반환할 값이 없으므로 void로 설정
        memoList.remove(id);
    }
}