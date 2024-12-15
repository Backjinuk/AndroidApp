
# 커뮤니티 시퀀스 다이어그램 인덱스

1. [커뮤니티 신청](#커뮤니티-신청)
2. [커뮤니티 목록 조회 및 승인, 거절](#커뮤니티-목록-조회-및-승인-거절)
3. [커뮤니티 생성](#커뮤니티-생성)
4. [커뮤니티 상세조회](#커뮤니티-상세조회)
5. [커뮤니티 목록 조회](#커뮤니티-목록-조회)
6. [커뮤니티 수정](#커뮤니티-수정)
7. [커뮤니티 삭제](#커뮤니티-삭제)
8. [커뮤니티 종료 후 리뷰 남기기](#커뮤니티-종료-후-리뷰-남기기)


## 커뮤니티 신청
```mermaid
sequenceDiagram
    actor 사용자
    participant CommunityApplyController
    participant CommunityApplyService
    participant NotificationService

    사용자->>CommunityApplyController: 커뮤니티 신청 요청 (커뮤니티 ID 등)
    CommunityApplyController->>CommunityApplyService: 커뮤니티 신청 요청 전달 (커뮤니티 ID 등)
    CommunityApplyService->>CommunityApplyService: 커뮤니티 인원수 확인 요청 (커뮤니티 ID)
    CommunityApplyService-->>CommunityApplyService: 현재 인원수 반환
    CommunityApplyService->>CommunityApplyService: 자동 승인 여부 결정
    alt 자동 승인
        CommunityApplyService->>CommunityApplyService: 최대 인원수 확인 (커뮤니티 ID)
        CommunityApplyService-->>CommunityApplyService: 인원수 초과 여부 반환 (초과/미초과)
        alt 인원수 미초과
            CommunityApplyService->>CommunityApplyService: 신청 상태 "승인"으로 설정
            CommunityApplyService->>CommunityApplyController: 신청 승인 완료 반환
            CommunityApplyController->>NotificationService: 신청 승인 알림 전송 요청 (사용자 대상)
            NotificationService->>CommunityApplyController: 알림 전송 완료
            CommunityApplyController->>사용자: 신청 승인 완료 메시지 반환
        else 인원수 초과
            CommunityApplyService->>CommunityApplyService: 신청 상태 "거절"으로 설정
            CommunityApplyService->>CommunityApplyController: 신청 거절 완료 반환
            CommunityApplyController->>NotificationService: 신청 거절 알림 전송 요청 (사용자 대상)
            NotificationService->>CommunityApplyController: 알림 전송 완료
            CommunityApplyController->>사용자: 신청 거절 메시지 반환 (인원 초과)
        end
    else 수동 승인 필요
        CommunityApplyService->>CommunityApplyService: 신청 상태 "대기 중"으로 설정
        CommunityApplyService->>CommunityApplyController: 신청 대기 상태 반환
        CommunityApplyController->>NotificationService: 관리자에게 신청 알림 전송 요청
        NotificationService->>CommunityApplyController: 알림 전송 완료
        CommunityApplyController->>사용자: 신청 대기 상태 메시지 반환
    end
```
## 커뮤니티 목록 조회 및 승인, 거절
```mermaid
sequenceDiagram
    actor 사용자
    participant CommunityApplyController
    participant CommunityApplyService
    participant NotificationService

    사용자->>CommunityApplyController: 신청 목록 조회 요청
    CommunityApplyController->>CommunityApplyService: 신청 목록 조회 요청
    CommunityApplyService->>CommunityApplyController: 신청 목록 반환
    CommunityApplyController->>사용자: 신청 목록 반환
    
    alt 요청 승인
        사용자->>CommunityApplyController: 신청 승인 요청 (신청 ID 등)
        CommunityApplyController->>CommunityApplyService: 신청 승인 요청 (신청 ID 등)
        CommunityApplyService->>CommunityApplyService: 신청 상태 "승인"으로 업데이트
        CommunityApplyService->>CommunityApplyController: 승인 처리 완료 반환
        CommunityApplyController->>NotificationService: 승인 알림 전송 요청 (사용자 대상)
        NotificationService->>CommunityApplyController: 알림 전송 완료
        CommunityApplyController->>admin: 승인 완료 메시지 반환
        CommunityApplyController->>사용자: 승인 완료 알림 전달
    else 요청 거절
        사용자->>CommunityApplyController: 신청 거절 요청 (신청 ID 등)
        CommunityApplyController->>CommunityApplyService: 신청 거절 요청 (신청 ID 등)
        CommunityApplyService->>CommunityApplyService: 신청 상태 "거절"으로 업데이트
        CommunityApplyService->>CommunityApplyController: 거절 처리 완료 반환
        CommunityApplyController->>NotificationService: 거절 알림 전송 요청 (사용자 대상)
        NotificationService->>CommunityApplyController: 알림 전송 완료
        CommunityApplyController->>admin: 거절 완료 메시지 반환
        CommunityApplyController->>사용자: 거절 완료 알림 전달
    end 
```
## 커뮤니티 생성
```mermaid
sequenceDiagram
    actor 사용자
    participant CommunityController
    participant CommunityService
    participant NotificationService

    사용자->>CommunityController: 커뮤니티 생성 요청 (제목, 설명, 카테고리, 위치 등)
    CommunityController->>CommunityService: 커뮤니티 생성 요청 전달 (제목, 설명, 카테고리, 위치 등)
    CommunityService->>CommunityService: 입력 데이터 검증
    alt 검증 성공
        CommunityService->>CommunityService: 커뮤니티 생성 (DB 저장)
        CommunityService->>CommunityController: 생성된 커뮤니티 정보 반환
        CommunityController->>NotificationService: 커뮤니티 생성 알림 전송 요청
        NotificationService->>CommunityController: 알림 전송 완료
        CommunityController->>사용자: 커뮤니티 생성 성공 메시지 반환
    else 검증 실패
        CommunityService->>CommunityController: 오류 메시지 반환
        CommunityController->>사용자: 커뮤니티 생성 실패 메시지 반환
    end
```

## 커뮤니티 상세조회
```mermaid
sequenceDiagram
    actor 사용자
    participant CommunityController
    participant CommunityService

    사용자->>CommunityController: 커뮤니티 상세 조회 요청 (커뮤니티 ID)
    CommunityController->>CommunityService: 커뮤니티 상세 조회 요청 전달 (커뮤니티 ID)
    CommunityService->>CommunityService: 커뮤니티 존재 여부 확인
    alt 커뮤니티 존재
        CommunityService->>CommunityService: 커뮤니티 정보 조회 (제목, 설명, 위치 등)
        CommunityService->>CommunityController: 조회된 커뮤니티 정보 반환
        CommunityController->>사용자: 커뮤니티 상세 정보 반환
    else 커뮤니티 미존재
        CommunityService->>CommunityController: 오류 메시지 반환
        CommunityController->>사용자: 커뮤니티 조회 실패 메시지 반환
    end
```

## 커뮤니티 목록 조회
```mermaid
sequenceDiagram
    actor 사용자
    participant CommunityController
    participant CommunityService

    사용자->>CommunityController: 커뮤니티 목록 조회 요청 (위치 범위, 카테고리 등 필터)
    CommunityController->>CommunityService: 커뮤니티 목록 조회 요청 전달 (위치 범위, 카테고리 등 필터)
    CommunityService->>CommunityService: 필터 조건에 맞는 커뮤니티 검색
    CommunityService->>CommunityService: 커뮤니티 목록 정렬 및 페이징 처리
    CommunityService->>CommunityController: 조회된 커뮤니티 목록 반환
    CommunityController->>사용자: 커뮤니티 목록 반환 (지도 표시용 데이터 포함)
```

## 커뮤니티 수정
```mermaid
sequenceDiagram
    actor 사용자
    participant CommunityController
    participant CommunityService
    participant NotificationService

    사용자->>CommunityController: 커뮤니티 수정 요청 (커뮤니티 ID, 수정할 정보)
    CommunityController->>CommunityService: 커뮤니티 수정 요청 전달 (커뮤니티 ID, 수정할 정보)
    CommunityService->>CommunityService: 입력 데이터 검증
    CommunityService->>CommunityService: 커뮤니티 존재 여부 확인
    alt 커뮤니티 존재 및 검증 성공
        CommunityService->>CommunityService: 커뮤니티 정보 수정 (DB 업데이트)
        CommunityService->>CommunityController: 수정된 커뮤니티 정보 반환
        alt 커뮤니티에 참석한 사람들 있을시
            CommunityController->>NotificationService: 커뮤니티 수정 알림 전송 요청
            NotificationService->>CommunityController: 알림 전송 완료
        end
        CommunityController->>사용자: 커뮤니티 수정 성공 메시지 반환
    else 커뮤니티 미존재 또는 검증 실패
        CommunityService->>CommunityController: 오류 메시지 반환
        CommunityController->>사용자: 커뮤니티 수정 실패 메시지 반환
    end
```
## 커뮤니티 삭제
```mermaid
sequenceDiagram
    actor 사용자
    participant CommunityController
    participant CommunityService
    participant NotificationService

    사용자->>CommunityController: 커뮤니티 삭제 요청 (커뮤니티 ID)
    CommunityController->>CommunityService: 커뮤니티 삭제 요청 전달 (커뮤니티 ID)
    CommunityService->>CommunityService: 커뮤니티 존재 여부 확인
    alt 커뮤니티 존재
        CommunityService->>CommunityService: 커뮤니티 삭제 (DB 삭제)
        CommunityService->>CommunityController: 삭제 완료 반환
        alt 커뮤니티에 참석한 사람이 있을시
            CommunityController->>NotificationService: 커뮤니티 삭제 알림 전송 요청
            NotificationService->>CommunityController: 알림 전송 완료
        end
        CommunityController->>사용자: 커뮤니티 삭제 성공 메시지 반환
    else 커뮤니티 미존재
        CommunityService->>CommunityController: 오류 메시지 반환
        CommunityController->>사용자: 커뮤니티 삭제 실패 메시지 반환
    end
```

## 커뮤니티 종료 후 리뷰 남기기
```mermaid

sequenceDiagram
    actor 사용자
    participant CommunityService
    participant ReviewService
    participant UserRatingService
    participant NotificationService

    사용자->>CommunityService: 리뷰 작성 요청 (모임 ID, 별점, 코멘트)
    CommunityService->>ReviewService: 리뷰 저장 요청 (모임 ID, 별점, 코멘트)
    ReviewService->>ReviewService: 리뷰 데이터 검증
    alt 검증 성공
        ReviewService->>ReviewService: 리뷰 데이터 DB 저장
        ReviewService->>UserRatingService: 사용자 평점 업데이트 요청 (사용자 ID, 별점)
        UserRatingService->>UserRatingService: USER_RATING 테이블 업데이트
        UserRatingService->>ReviewService: 평점 업데이트 완료
        ReviewService->>NotificationService: 리뷰 작성 알림 요청 (관련 사용자 대상)
        NotificationService->>ReviewService: 알림 전송 완료
        ReviewService->>CommunityService: 리뷰 저장 성공 반환
        CommunityService->>사용자: 리뷰 작성 성공 메시지 반환
    else 검증 실패
        ReviewService->>CommunityService: 오류 메시지 반환
        CommunityService->>사용자: 리뷰 작성 실패 메시지 반환
    end
```
