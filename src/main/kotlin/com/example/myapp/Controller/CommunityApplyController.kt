package com.example.myapp.Controller

import com.example.myapp.Service.CommunityApply.CommunityApplyService
import com.example.myapp.Util.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController

@RestController
class CommunityApplyController @Autowired constructor(
    private val communityApplyService: CommunityApplyService,
    private val jwtUtil: JwtUtil
) {



}