package com.example.myapp.Dto

import java.time.LocalDateTime

class SubscribeDto {

    var subscribeSeq : Long ?= 0

    var subscriberOwnerUserSeq : Long ?= 0

    var subscriberUserSeq: Long ?= 0

    var subscribeStatus:Char ?= null

    var regDt: LocalDateTime?= LocalDateTime.now()
}