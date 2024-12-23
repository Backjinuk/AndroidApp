# API 정의서

### 1. 사용자 로그인
**Endpoint**: `POST /user/userLogin`  
**Description**: 이메일, 패스워드를 통해 사용자 로그인 시도

**Request:**
```json
{
    "userEmail": "example@google.com",
    "password" : "password"
}
```

**Response:**
```json
성공여부
```

### 2. 사용자 회원가입
**Endpoint**: `POST /user/userJoin`  
**Description**: 회원가입 

**Request:**
```json
{
  "email": "",
  "password": "",
  "phoneNum": "",
  "nickName": "",
  "bio" : ""
}
```

**Response:**
```json
회원가입 성공시 어떤 Response가 좋을지 고민이네요
```
**결정 필요사항** : <br> 1.user_profile 의 내용까지 회원가입시 같이 넣을것인지, 가입 이후 프로필설정을 바로할수 있도록 유도할것인지
<br>2. 회원가입 시 중복 사용자를 어떻게 구분할것인지
<br>3. 회원가입 Response 에서 어떤 정보를 넣을것인지
### 3. 사용자 정보 수정
**Endpoint**: `PUT /user/editUserInfo`  
**Description**: 사용자 정보 수정

**Request:**
```json
{
  "email": "",
  "password": "",
  "phoneNum": "",
  "nickName": "",
  "bio" : "",
  "profileImg" : "",
  "interest" : "",
  "experience" : "",
  "location" : "",
  "socialLinks" : ""
}
```

**Response:**
```json
회원가입 성공시 어떤 Response가 좋을지 고민이네요
```
**결정 필요사항** : user_profile 의 내용까지 회원가입시 같이 넣을것인지, 가입 이후 프로필설정을 바로할수 있도록 유도할것인기

### 4. 커뮤니티 목록 조회
**Endpoint**: `GET /community/getCommunityList`  
**Description**: 커뮤니티 목록 조회

**Request:**
```json
{
  "lalitude": "",
  "longitude": "",
  "page": "",
  "category": ""
}
```
조회를 어떤 기준으로 하는지?

**Response:**
```json
{
  "commu_title" : "",
  "commu_comment": "",
  "category": "",
  "reg_dt": "",
  "update_dt": "",
  "status": ""
}
```
**결정 필요사항** : 데려올 컬럼 확정필요

### 5. 커뮤니티 생성
**Endpoint**: `POST /community/addCommunity`  
**Description**: 커뮤니티 등록

**Request:**
```json
{
    "commu_title" : "",
    "commu_comment": "",
    "category": "",
    "reg_dt": "",
    "update_dt": "",
    "status": "",
    "lalitude": "",
    "longitude": "",
}
```
생성 시 필요정보 추가여부 필수

**Response:**
```json
True or False
```
**결정 필요사항** : 생성 후 알림을 어떻게받을지(계속 작성하다보니 response 기본 템플릿을 정해야될거같네요)

### 6. 커뮤니티 신청
**Endpoint**: `GET /community/getCommunityList`  
**Description**: 커뮤니티 목록 조회

**Request:**
```json
{
  "user_seq": "",
  "commu_seq": ""
}
```

**Response:**
```json

```

### 7. 커뮤니티 신청 이력 목록 (사용자)
**Endpoint**: `GET /community/applyCommunityList`  
**Description**: 커뮤니티 목록 조회

**Request:**
```json
{
  "user_seq": "",
  "category": ""
}
```

**Response:**
```json
{
  List : [
    {
      "commu_title": "",
      "commu_comment": "",
      "apply_status": "",
      "apply_time": ""
    }
  ]
}
```
**결정 필요사항** : 이력 목록이 필요해 보여서 추가한 api 입니다 간단하게 목록을 보여주는데 어떤정보가
더 필요할지 결정이 필요합니다.


### 8. 커뮤니티 신청 목록 (커뮤니티 소유자가)
**Endpoint**: `GET /community/getCommunityList`  
**Description**: 커뮤니티 목록 조회

**Request:**
```json
{
  "commu_user_seq": "소유자 seq", 
  "apply_user_seq": "신청자 seq",
  "category": ""
}
```

**Response:**

```json
{
  "userList": [
    {
      "user_seq": "",
      "nick_name": "",
      "apply_status": "",
      "apply_time": ""
    }
  ]
}
```

### 9. 커뮤니티 승인 (커뮤니티 소유자가)
**Endpoint**: `PATCH /community/applyUser`  
**Description**: 커뮤니티 목록 조회

**Request:**
```json
{
  "user_seq": "",
  "apply_seq": ""
}
```

**Response:**
```json
True or False
```
