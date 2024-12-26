package com.example.myapp.user.user.domain.entity

import com.example.myapp.user.user.domain.UserJoinType
import com.example.myapp.user.user.domain.UserRole
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.Data

@Entity
@Data
class UserEntity {

    @Id
    @GeneratedValue(generator = "user_seq", strategy = GenerationType.IDENTITY)
    open var userSeq:Long = 0

    open var fileSeq:Long = 0; // 물리적 연결을 안맺은

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    open var email: String = ""

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8~20자 사이여야 합니다.")
    open var passwd: String = ""

    @NotBlank(message = "닉네임은 필수 항목입니다.")
    open var nickName: String = ""

    open var ciKey:String = "" // 소셜 미디어로 로그인시 받는 키값


    @Enumerated(EnumType.STRING)
    open var joinType:UserJoinType = UserJoinType.HOMPAGE // 어떤 경로로 로그인 했는지 파악

    @Enumerated(EnumType.STRING)
    open var userRole:UserRole = UserRole.User
}