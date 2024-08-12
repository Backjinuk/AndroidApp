package com.example.myapp.Dto

import java.time.LocalDateTime

class UserTokenDto {

    var userTokenSeq:Long ?= 0;

    var refreshUserToken:String ?= "";

    var user : UserDto ?= UserDto();

    var regDt : LocalDateTime ?= LocalDateTime.now();

}