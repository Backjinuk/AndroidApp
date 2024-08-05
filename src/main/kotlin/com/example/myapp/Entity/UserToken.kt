package com.example.myapp.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class UserToken {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userTokenSeq:Long ?= 0;

    @Column(columnDefinition = "TEXT")
    var refreshUserToken: String? = ""

    @OneToOne
    @JoinColumn(name="usrSeq")
    var user:User ?= User();

    var regDt: LocalDateTime?= LocalDateTime.now()

}