package com.example.myapp.Entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@SequenceGenerator(
    name = "user_token_seq_generator",
    sequenceName = "user_token_seq",
    allocationSize = 1
)
open class UserToken {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_token_generator")
    @Column(name = "user_token_seq")
    open var userTokenSeq:Long ?= 0;

    @Column(columnDefinition = "TEXT")
    open var refreshUserToken: String? = ""

    @OneToOne
    @JoinColumn(name="usrSeq")
    open var user:User ?= User();

    open var regDt: LocalDateTime?= LocalDateTime.now()

}