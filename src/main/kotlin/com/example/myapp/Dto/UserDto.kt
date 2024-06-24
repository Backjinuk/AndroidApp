package com.example.myapp.Dto

import java.time.LocalDateTime
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local


class UserDto {
    var userId: String? = null
    var name: String? = null
    var passwd: String? = null
    var phoneNum: String? = null
    var rNum: String? = null
    var email: String? = null
    var crtDt :LocalDateTime? = LocalDateTime.now()
}