package com.example.myapp.Controller

import com.example.myapp.Service.Community.CommunityService
import com.example.myapp.Service.User.UserService
import com.example.myapp.Util.JwtUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommunityContollerTest (
    private var communityService: CommunityService,
    private var userService: UserService,
    private var jwtUtil: JwtUtil
){
    @Test
    fun getLocationBaseInquery() {

//        communityService.getLocationBaseInquey()
    }
}