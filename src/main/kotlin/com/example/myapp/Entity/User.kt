package com.example.myapp.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@SequenceGenerator(
    name = "user_seq",
    sequenceName = "user_seq_generator",
    allocationSize = 1
)
open class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
    @Column(name = "user_seq")
    open var userSeq: Long? = 0

    open var userId: String? = ""

    open var name: String? = ""

    open var passwd: String? = "" // 해당 필드를 암호화된 버전으로 교체해야 합니다.

    open var phoneNum: String? = ""

    open var email: String? = ""

    open var usertype : String?= ""

    open var crtDt :LocalDateTime? = LocalDateTime.now()

}