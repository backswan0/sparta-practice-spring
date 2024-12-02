package com.example.memo.controller;

/*
[해결한 문제]
[1] 응답 코드가 세분화 되었다.
[2] 적절한 예외를 처리했다.
 */

import com.example.memo.dto.MemoRequestDto;
import com.example.memo.dto.MemoResponseDto;
import com.example.memo.entity.Memo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// @Controller + @ResponseBody 기능 == @RestController

@RestController
@RequestMapping("/memos")
public class MemoController {
    private final Map<Long, Memo> memoList = new HashMap<>();

    // ----------------- 메모 생성 API 수정하기 (CREATE) -----------------
    @PostMapping
    public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequestDto dto) {
        /*
        [상태 코드 따로 반환해주기]
        [수정 전] public MemoResponseDto
        [수정 후] ResponseEntity<MemoResponseDto>
         */
        Long memoId = memoList.isEmpty() ? 1 : Collections.max(memoList.keySet()) + 1;
        Memo memo = new Memo(memoId, dto.getTitle(), dto.getContents());
        memoList.put(memoId, memo);

        return new ResponseEntity<>(new MemoResponseDto((memo)), HttpStatus.CREATED);
        /*
        [상태 코드 따로 반환해주기]
        [수정 전] return new MemoResponseDto(memo);
        [수정 후] return new ResponseEntity<>(new MemoResponseDto((memo)), HttpStatus.CREATED);
                ➡️ 실제로 응답할 HttpStatus 코드를 넣어주었다.
         */
    }

    // ----------------- 메모 전체 목록 조회 API 추가하기 (READ) -----------------
    @GetMapping
    public ResponseEntity<List<MemoResponseDto>> findAllMemos() {
        // 전체 조회라서 별도의 파라미터(매개변수)가 필요 없다.

        // [1] List 초기화
        List<MemoResponseDto> responseDtoList = new ArrayList<>();

        // [2] HashMap<Memo>로 저장된 걸 전체 조회해서 List<MemoResponseDto> 형태로 만들어줘야 한다.
        for (Memo memo : memoList.values()) {
            MemoResponseDto responseDto = new MemoResponseDto(memo);
            // 미리 만들어둔 생성자를 쓰는 것이다.

            responseDtoList.add(responseDto);
        }
        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
        /*
         responseDtoList == 응답값의 body
         return new ResponseEntity<>(HttpStatus.OK);
         == 아무것도 없지만 정상적으로 작동했어. OK이야.
         근데 GET인데 반환 값이 없는 게 논리적으로 맞는가???? 아니다..!
         ➡️안 쓴다.
         ➡️서버가 살았는지 죽었는지 확인할 때 쓴다. 왜냐하면, 서버가 정상적으로 돌아가는지만 보면 되니까.
         */
    }
    /*
    [for문 대신 Map to List: stream()을 사용한다면?]
    responseDtoList = memoList.values().stream().map(MemoResponseDto::new).toList();
    return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
     */

    // ----------------- 메모 단건 조회 기능 수정하기 (READ) -----------------
    @GetMapping("/{id}")
    public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id) {
        /*
        [수정 전] public MemoResponseDto findMemoById(@PathVariable Long id)
        [수정 후] public ResponseEntity<MemoResponseDto> findMemoById(@PathVariable Long id)
         */
        Memo memo = memoList.get(id);

        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        /*
        [수정 전] if문 없었음
        [수정 후] if문을 추가하여 조회되는 메모가 없다면 404 오류 메시지가 나오도록 함
         */

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
        /*
        [수정 전] return new MemoResponseDto(memo);
        [수정 후] return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
         */
    }

    // ----------------- 메모 단건의 title과 contents 둘 다 수정하기 (UPDATE-PUT) -----------------
    @PutMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateMemoById(@PathVariable Long id, @RequestBody MemoRequestDto dto) {
        /*
        [수정 전] public MemoResponseDto updateMemoById(@PathVariable Long id, @RequestBody MemoRequestDto dto)
        [수정 후] public ResponseEntity<MemoResponseDto> updateMemoById(@PathVariable Long id, @RequestBody MemoRequestDto dto)
         */

        Memo memo = memoList.get(id);

        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        /*
        [수정 전] if문 없었음
        [수정 후] if문을 추가하여 조회되는 메모가 없다면 404 오류 메시지가 나오도록 함
         */

        if (dto.getTitle() == null || dto.getContents() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        /*
        [필수 값을 검증하는 부분]
        [참고] 필수 값인 요청 데이터는 MemoRequestDto 안에 있음
        dto == @RequestBody MemoRequestDto dto
         */

        memo.update(dto);

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
        /*
        [수정 전] return new MemoResponseDto(memo);
        [수정 후] return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
         */
    }

    // ----------------- 메모 단건의 title과 contents 중에서 title만 수정하기 (UPDATE-PATCH) -----------------
    @PatchMapping("/{id}")
    public ResponseEntity<MemoResponseDto> updateTitle(@PathVariable Long id, @RequestBody MemoRequestDto dto) {
        // 요구사항에 요청 데이터가 있으므로 두 번째 매개변수로 @RequestBody MemoRequestDto dto 추가

        Memo memo = memoList.get(id);
                /*
                [데이터베이스에서 조회]
                저장된 memoList에서 메모를 꺼내와야 메모를 수정할 수 있으니까 위와 같이 작성
                 */
        if (memo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (dto.getTitle() == null || dto.getContents() != null) {
            /*
            [1] 제목을 수정하려는데 null이면 안 되므로 dto.getTitle() == null
            [2] dto.getContents() != null 설명
            - MemoRequestDto 필드는 총 두 가지임: title, contents
            - contents는 제목 수정에 필요 없음. 즉 있으면 안 됨
            - 따라서 위 코드는 title이 null이거나 contents가 있으면 Bad Request라는 뜻
             */
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        memo.updateTitle(dto);
        // 위의 if문 모두 작성 후 Memo 클래스에서 updateTitle() 메서드 추가 및 호출하기

        return new ResponseEntity<>(new MemoResponseDto(memo), HttpStatus.OK);
    }

    // ----------------- 메모 삭제 API 수정하기 (DELETE) -----------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long id) {
        /*
        [수정 전] void
        [수정 후] ResponseEntity<Void>
        [이유] 데이터를 응답할 필요가 없어서 Generic으로 Void를 넣어줌
         */

        /*
        ['memoList의 Key 값에 id값이 포함되었응면 검사 가능'을 코드로 구현하기]
        아래와 같은 if문과 if문이 없을 반환할 응답 상태 추가
         */
        if (memoList.containsKey(id)) {
            memoList.remove(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        // 검사 기능이 작동하지 않을 때
    }
}

/*
[주석 모음]

[절대 잊으면 안 되는 것]
첫째, src>test>java에 클래스를 넣지 않았는지 확인하자
     ➡️src>main>java이다. 그렇게 안 하고 postman을 실행하면 404 오류 뜬다.
둘째, import가 두 번 되지 않았는지 꼭 살펴보자.

[@RequestMapping]
- @PostMapping 사용 후 @RequestMapping을 추가하자.
- prefix하는 URL을 설정할 때 사용한다.
  == prefix처럼 하위 메서드 전체에 영향을 주기 때문에 'prefix'라고 표현함
- 공통적으로 들어가는 /memos라는 URL을 적어둔다.

[Long의 의미]
private final Map<Long, Memo> memoList = new HashMap<>();
여기서 key 값이 바로 Long이다.

[@PostMapping]
- create를 담당한다.
- @PostMapping("/healthy") 이렇게 적으면?
  == localhost:8080/memos/healthy
  == 영향 범위는 메서드의 블럭만. 즉 다른 메서드에 영향을 주려면 해당 메서드 위에 다시 annotation을 써야 한다.

[createMemo() 메서드 안에서 해야 할 일]
[1] 식별자가 1씩 증가해야 함. 중복이 되면 안 되니까
[2] 요청받은 데이터로 Memo를 생성해야
[3] Inmemory DB에 메모 저장 == 임의로 데이터베이스를 사용하는 게 아니라, Java의 Map 자료구조를 이용하는 것
    == 자료구조라서 프로젝트를 실행했다가 종료하면 그 안의 모든 데이터가 지워질 거라서 Inmemory라고 표현
[4] 저장된 데이터를 MemoResponseDto 형태로 반환하기

[createMemo() 메서드 안에서 해야 할 일 1]
(a) memoList.isEmpty(): 비어 있는지 검사
(b) 1: 비어 있다면 1로 초기 값 설정
(c) Collections.max(memoList.keySet()): memoList.keySet() 안에 든 값 중 최댓값을 뽑아내는 메서드
(d) memoList.keySet(): memoList 안에 있는 key 값을 전부 뽑아내는 메서드
    == 모든 Long 값을 꺼내서 최댓값을 반환해주는 메서드
    우리는 1씩 증가해야 하므로 + 1 추가
    이후 추가: Long memoId =

[createMemo() 메서드 안에서 해야 할 일 2]
- MemoRequestDto 형태로 요청 받았기 때문에 Memo 객체로 바꿔줘야 한다.
- 첫 번째 인자로 memoId 값 전달
- 두 번째로 title과 세 번째로 contents를 전달해주어야 하는데, 두 데이터 모두 dto 안에 있으므로 getter 사용
- 이후 Memo 타입 변수로 받아주기. 즉, 'Memo memo = ' 추가

[createMemo() 메서드 안에서 해야 할 일 3]
- key 값은 memoId
- 저장될 객체 형태는 memo

[createMemo() 메서드 안에서 해야 할 일 4]
- MemoResponseDto 클래스로 돌아가서 생성자를 만들어준다.
- return new MemoResponseDto(); 입력하기
- 그다음, 위와 같이 적은 곳의 () 안에 memo를 넣어준다.

[@GetMapping]
- 이미 prefix로 /memos를 적었으므로 /{id}와 같이 식별자를 추가로 적는다.
- 식별자를 파라미터(매개변수)로 바인딩할 때는 @PathVariable을 사용한다.

[@PutMapping]
- 전체 수정이므로 Put이다.
- 단건을 수정해야 하므로 아까처럼 경로변수가 필요하다.
- id값 뿐만 아니라 우리가 어떤 데이터로 수정할지 그 데이터도 요청 받아야 한다.
  ➡️RequestDto 형태로 받기로 한 걸 기억하자!
  ➡️지금은 title과 contents 둘 다 수정할 수 있다고 가정했다.
  == @PathVariable Long id 옆에 @RequestBody MemoRequestDto dto를 작성한 이유

[updateMemoById() 메서드에 작성해야 하는 부분]
- Memo memo = memoList.get(id);
  ➡️수정하려면 우선 저장된 메모를 꺼내와야 하므로 위와 같이 작성한다.
  ➡️이후 Memo 클래스로 돌아가서 update() 메서드를 새로 만들자.

[deleteMemo() 메서드 특징]
- CRUD의 DELETE 가능이라서 따로 반환할 값이 없으므로 void로 설정한다.
  */