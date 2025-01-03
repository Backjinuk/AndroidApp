package com.example.myapp.user.user.domain.entity

import jakarta.persistence.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

@Entity
class UserTokenEntity {

    @Id
    @GeneratedValue(generator = GenerationType.SEQUENCE, strategy = "user_token_seq")
    @SequenceGenerator(name = "user_token_seq", sequenceName = "user_token_seq", allocationSize = 1)
    open var userTokenSeq: Long? = 0

    @field:Min(0, message = "유저 시퀸스는 양수여야 합니다.")
    open var userSeq: Long = 0

    @field:NotBlank(message = "refreshToken은 비어있을수 없습니다.")
    open var refreshToken: String = ""

    @field:NotBlank(message = "만료시간은 비어있을수 없습니다.")
    open var expiredDt: LocalDateTime? = null

    open var regDt: LocalDateTime = LocalDateTime.now()
}