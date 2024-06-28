package com.example.myapp.Entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.cglib.core.Local
import java.time.LocalDateTime

@Entity
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var usrSeq: Long? = 0
    var userId: String? = ""
    var name: String? = ""
    var passwd: String? = "" // 해당 필드를 암호화된 버전으로 교체해야 합니다.
    var phoneNum: String? = ""
    var email: String? = ""
    var usertype : String?= ""
    var crtDt :LocalDateTime? = LocalDateTime.now()

}