package com.example.myapp.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@SequenceGenerator(
    name = "user_token_seq_generator",
    sequenceName = "user_token_seq",
    allocationSize = 1
)
class UserToken {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_token_generator")
    @Column(name = "user_token_seq")
    var userTokenSeq:Long ?= 0;

    @Column(columnDefinition = "TEXT")
    var refreshUserToken: String? = ""

    @OneToOne
    @JoinColumn(name="usrSeq")
    var user:User ?= User();

    var regDt: LocalDateTime?= LocalDateTime.now()

}