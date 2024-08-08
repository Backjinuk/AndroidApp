package com.example.myapp.Controller

import com.example.myapp.Entity.Community
import com.example.myapp.Service.Community.CommunityService
import com.example.myapp.Service.User.UserService
import com.example.myapp.Util.JwtUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommunityControllerTest {

    @Autowired
    private lateinit var communityService: CommunityService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Test
    fun getLocationBaseInquery() {
        val commuList: List<Community> = communityService.getLocationBaseInquey(37.48838166666667, 126.90010166666667, 0.3)

        for (community in commuList) {
            println("Community = $community")
        }
    }
}
