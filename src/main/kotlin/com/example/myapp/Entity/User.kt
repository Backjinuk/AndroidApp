package com.example.myapp.Entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val usrId: Long? = null

    val usrPwd : String? = null;

    val usrName : String? = null;

    val crtDt : LocalDateTime? = null;




}