package com.example.myapp.Dto

import java.time.LocalDateTime

class CommunityApplyDto {

    var communityApplySeq : Long ?= 0

    var applyUserSeq : Long ?= 0

    var applyCommuSeq : Long ?= 0

    var applyStatus: Char ?= 'N'

    var regDt:LocalDateTime ?= LocalDateTime.now()

    var upDt:LocalDateTime ?= null

}