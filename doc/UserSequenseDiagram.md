# 프로젝트 시퀀스 다이어그램 인덱스

1. [회원가입](#회원가입)
2. [로그인](#로그인)
3. [회원정보 수정](#회원정보-수정)
4. [회원 삭제](#회원-삭제)
5. [회원 세팅 수정](#회원-세팅-수정)
6. [회원 프로필 조회](#회원-프로필-조회)
7. [회원 프로필 수정](#회원-프로필-수정)
8. [구독 신청](#구독-신청)
9. [구독 목록 조회](#구독-목록-조회)
10. [구독 수정](#구독-수정)
11. [사용자 랭킹 조회](#사용자-랭킹-조회)
12. [채팅방 생성](#채팅방-생성)
13. [채팅방 참여](#채팅방-참여)
14. [메시지 전송](#메시지-전송)
15. [신고 신청](#신고-신청)
16. [신고 처리](#신고-처리)
---

## 회원가입

```mermaid
sequenceDiagram
    actor 사용자
    participant UserController
    participant UserService
    participant UserSettingService
    participant TokenService
    participant UserProfileService

    사용자->>UserController: 회원가입 요청 (이메일, 비밀번호 등)
    UserController->>UserService: 이메일, 비밀번호 검증
    UserService->>UserController: 검증 결과 반환
    alt 검증 성공
        UserController->>UserService: 사용자 생성 요청
        UserService->>UserSettingService: 사용자 기본 세팅 생성 요청
        UserSettingService->>UserSettingService: 사용자 기본 세팅 생성
        UserSettingService->>UserService: 사용자 기본 세팅 정보 반환
        UserService->>UserProfileService: 사용자 기본 프로필 생성 요청
        UserProfileService->>UserProfileService: 사용자 기본 프로필 생성
        UserProfileService->>UserService: 사용자 기본 프로필 정보 반환
        UserService->>UserController: 생성된 사용자 반환
        UserController->>TokenService: JWT 토큰 생성 요청
        TokenService->>TokenService: refreshToken DB 저장 요청
        TokenService->>UserController: 생성된 JWT 토큰 반환
        TokenService->>UserController: 저장된 refreshToken 반환
        UserController->>사용자: 성공 메시지 반환
    else 검증 실패
        UserController->>사용자: 오류 메시지 반환
    end
```

## 로그인
```mermaid
sequenceDiagram
    actor 사용자
    participant UserController
    participant TokenService
    participant UserService
    participant UserSettingService

    사용자->>UserController: 로그인 요청 (이메일, 비밀번호)
    UserController->>TokenService: 사용자 인증 요청 (이메일, 비밀번호)
    TokenService->>UserController: 인증 결과 반환 (성공/실패)
    alt 인증 성공
        UserController->>UserService: 사용자 정보 조회 요청
        UserService->>UserSettingService: 사용자 설정 정보 조회 요청
        UserSettingService->>UserService: 사용자 설정 정보 반환
        UserService->>UserController: 사용자 정보 반환
        UserController->>TokenService: 토큰 정보 수정 요청 (만료시간 등)
        TokenService->>TokenService: 수정된 토큰 정보 수정
        TokenService->>UserController: 수정된 JWT 토큰 반환
        UserController->>사용자: 수정된 JWT 토큰 및 사용자 정보 반환
    else 인증 실패
        UserController->>사용자: 인증 실패 메시지 반환
    end
```

## 회원정보 수정
```mermaid
sequenceDiagram
    actor 사용자
    participant UserController
    participant UserService

    사용자->>UserController: 회원 정보 수정 요청 (이메일, 비밀번호 등)
    UserController->>UserService: 회원 정보 수정 요청
    UserService->>UserController: 수정된 회원 정보 반환
    UserController->>사용자: 수정된 회원 정보 반환
```

## 회원 삭제
```mermaid
sequenceDiagram
    actor 사용자
    participant UserController
    participant UserService
    participant UserSettingService
    participant UserProfileService
    participant TokenService

    사용자->>UserController: 계정 삭제 요청
    UserController->>UserService: 계정 삭제 요청
    UserService->>UserSettingService: 유저 세팅 삭제 요청
    UserSettingService->>UserSettingService:유저 세팅 삭제 
    UserSettingService->>UserService: 유저 세팅 삭제 성공 반환
    UserService->>UserProfileService: 유저 프로필 삭제 요청
    UserProfileService->>UserProfileService: 유저 프로필 삭제
    UserProfileService->>UserService: 삭제 성공 반환
    UserService->>UserController: 계정 삭제 확인
    UserController->>TokenService: 관련된 refreshToken 삭제 요청
    TokenService->>UserController: refreshToken 삭제 확인
    UserController->>사용자: 계정 삭제 완료 메시지 반환
```
## 회원 세팅 수정
```mermaid
sequenceDiagram
    actor 사용자
    participant UserController
    participant SettingsService

    사용자->>UserController: 회원 세팅 수정 요청 (알림 설정, 언어 설정, 테마 설정 등)
    UserController->>SettingsService: 회원 세팅 수정 요청
    SettingsService->>UserController: 수정된 회원 세팅 반환
    UserController->>사용자: 수정된 회원 세팅 반환
```

## 회원 프로필 조회
```mermaid
sequenceDiagram
    actor 사용자
    participant ProfileController
    participant ProfileService
    participant FileService

    사용자->>ProfileController: 프로필 조회 요청
    ProfileController->>ProfileService: 프로필 조회 요청
    ProfileService->>FileService: 프로필 사진 요청
    FileService->>ProfileService: 프로필 사진 반환
    ProfileService->>ProfileController: 프로필 정보 반환
    ProfileController->>사용자: 프로필 정보 반환
```

## 회원 프로필 수정

```mermaid
sequenceDiagram
    actor 사용자
    participant ProfileController
    participant ProfileService
    participant FileService

    사용자->>ProfileController: 프로필 조회 요청
    ProfileController->>ProfileService: 프로필 조회 요청
    ProfileService->>FileService: 프로필 사진 요청
    FileService->>ProfileService: 프로필 사진 반환
    ProfileService->>ProfileController: 프로필 정보 반환
    ProfileController->>사용자: 프로필 정보 반환
```

## 구독 신청
```mermaid
sequenceDiagram
    actor 사용자
    participant SubscribeController
    participant SubscribeService
    participant NotificationService

    사용자->>SubscribeController: 구독 생성 요청 (커뮤니티 ID 등)
    SubscribeController->>SubscribeService: 구독 생성 요청 (커뮤니티 ID 등)
    SubscribeService->>SubscribeController: 구독 생성 완료
    SubscribeController->>NotificationService: 구독 알림 전송 요청
    NotificationService->>SubscribeController: 알림 전송 완료
    SubscribeController->>사용자: 구독 완료 메시지 반환
```

## 구독 목록 조회
```mermaid
sequenceDiagram
    actor 사용자
    participant SubscribeController
    participant SubscribeService

    사용자->>SubscribeController: 구독 목록 조회 요청
    SubscribeController->>SubscribeService: 구독 목록 조회 요청
    SubscribeService->>SubscribeController: 구독 목록 반환
    SubscribeController->>사용자: 구독 목록 반환
```

## 구독 수정
```mermaid
sequenceDiagram
    actor 사용자
    participant SubscribeController
    participant SubscribeService

    사용자->>SubscribeController: 구독 수정 요청 (구독 상태 변경 등)
    SubscribeController->>SubscribeService: 구독 수정 요청 (구독 상태 등)
    SubscribeService->>SubscribeController: 수정된 구독 정보 반환
    SubscribeController->>사용자: 수정된 구독 정보 반환
```

## 사용자 랭킹 조회
```mermaid
sequenceDiagram
    actor 사용자
    participant UserController
    participant UserRatingService

    사용자->>UserController: 사용자 평점 조회 요청 (사용자 ID)
    UserController->>UserRatingService: 사용자 평점 조회 요청 전달 (사용자 ID)
    UserRatingService->>UserRatingService: USER_RATING 테이블 조회
    UserRatingService->>UserController: 조회된 평점 정보 반환
    UserController->>사용자: 사용자 평점 정보 반환
```

## 채팅방 생성
```mermaid
sequenceDiagram
    actor 사용자
    participant ChatController
    participant ChatService
    participant CommunityService
    participant NotificationService

    사용자->>ChatController: 채팅방 생성 요청 (커뮤니티 ID 등)
    ChatController->>ChatService: 채팅방 생성 요청 (커뮤니티 ID 등)
    ChatService->>CommunityService: 커뮤니티 설정 조회 요청 (커뮤니티 ID)
    CommunityService-->>ChatService: 채팅 가능 여부 반환 (예/아니오)
    alt 채팅 가능
        ChatService->>ChatController: 채팅방 생성 완료 (채팅방 ID 등)
        ChatController->>NotificationService: 채팅방 생성 알림 전송 요청
        NotificationService-->>ChatController: 알림 전송 완료
        ChatController->>사용자: 채팅방 생성 완료 메시지 반환
    else 채팅 불가능
        ChatService->>ChatController: 채팅 불가능 오류 반환
        ChatController->>사용자: 채팅 불가능 메시지 반환
    end
```
## 채팅방 참여
```mermaid
sequenceDiagram
    actor 사용자
    participant ChatController
    participant ChatService

    사용자->>ChatController: 채팅방 참여 요청 (채팅방 ID 등)
    ChatController->>ChatService: 채팅방 참여 요청 (채팅방 ID 등)
    ChatService->>ChatService: 채팅방 메시지 조회
    ChatService->>ChatController: 메시지 목록 반환
    ChatController->>사용자: 채팅방 정보 반환
```

## 메시지 전송
```mermaid
sequenceDiagram
    actor 사용자
    participant ChatController
    participant ChatService
    participant NotificationService

    사용자->>ChatController: 메시지 전송 요청 (채팅방 ID, 메시지 내용 등)
    ChatController->>ChatService: 메시지 전송 요청 (채팅방 ID, 메시지 내용 등)
    ChatService->>ChatController: 메시지 전송 완료
    ChatController->>NotificationService: 메시지 전송 알림 요청
    NotificationService->>ChatController: 알림 전송 완료
    ChatController->>사용자: 메시지 전송 완료 메시지 반환
```

## 신고 신청
```mermaid
sequenceDiagram
    actor 사용자
    participant ReportController
    participant ReportService
    participant NotificationService
    participant AdminService

    사용자->>ReportController: 신고 요청 (모임 ID, 신고 사유 등)
    ReportController->>ReportService: 신고 저장 요청 (모임 ID, 신고 사유 등)
    ReportService->>ReportService: 신고 데이터 검증
    alt 검증 성공
        ReportService->>ReportService: 신고 데이터 DB 저장
        ReportService->>AdminService: 관리자에게 신고 알림 요청 (신고 ID, 모임 ID 등)
        AdminService->>NotificationService: 관리자에게 신고 알림 전송 요청
        NotificationService->>AdminService: 알림 전송 완료
        ReportService->>ReportController: 신고 저장 성공 반환
        ReportController->>NotificationService: 사용자에게 신고 접수 알림 요청
        NotificationService->>ReportController: 알림 전송 완료
        ReportController->>사용자: 신고 접수 성공 메시지 반환
    else 검증 실패
        ReportService->>ReportController: 오류 메시지 반환
        ReportController->>사용자: 신고 접수 실패 메시지 반환
    end
    alt 신고자에게 추가 알림 필요 시
        ReportController->>NotificationService: 추가 알림 요청
        NotificationService->>ReportController: 알림 전송 완료
    end
```

## 신고 처리
```mermaid
sequenceDiagram
    actor 관리자
    participant ReportController
    participant ReportService
    participant NotificationService

    관리자->>ReportController: 신고 목록 조회 요청
    ReportController->>ReportService: 신고 목록 조회 요청
    ReportService->>ReportService: 신고 목록 검색 및 정렬
    ReportService->>ReportController: 신고 목록 반환
    ReportController->>관리자: 신고 목록 반환

    관리자->>ReportController: 신고 처리 요청 (신고 ID, 처리 결과 등)
    ReportController->>ReportService: 신고 처리 요청 전달 (신고 ID, 처리 결과 등)
    ReportService->>ReportService: 신고 상태 업데이트 (승인/거절)
    ReportService->>ReportController: 신고 처리 완료 반환
    ReportController->>NotificationService: 신고 처리 결과 알림 전송 요청 (신고자 대상)
    NotificationService->>ReportController: 알림 전송 완료
    ReportController->>관리자: 신고 처리 완료 메시지 반환
    ReportController->>사용자: 신고 처리 결과 알림 전달
```