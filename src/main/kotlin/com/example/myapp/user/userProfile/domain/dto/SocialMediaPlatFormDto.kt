package com.example.myapp.user.userProfile.domain.dto

import jakarta.validation.constraints.Min

open class SocialMediaPlatFormDto {

    open var socialMediaPlatFormSeq:Long = 0

    @field:Min(0, message = "유저 프로필의 시퀸스는 0이상이여야 합니다.")
    open var userProfileSeq: Long = 0;

    open var platFormName: String = "";

    open var platFormUrl: String = "";

}