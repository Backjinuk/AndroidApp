package com.example.myapp.Dto

import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local


class UserDto {
    var userSeq : Long? = 0
    var userId: String? = ""
    var name: String? = ""
    var passwd: String? = ""
    var phoneNum: String? = ""
    var email: String? = ""
    var userType : String? = ""
    var crtDt :LocalDateTime? = LocalDateTime.now()
}