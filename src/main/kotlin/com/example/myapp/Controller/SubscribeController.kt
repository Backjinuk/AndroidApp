package com.example.myapp.Controller

import com.example.myapp.Dto.SubscribeDto
import com.example.myapp.Service.Subscribe.SubscribeService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.subscribeDtoToEntity
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/subscribe/")
class SubscribeController @Autowired constructor(
    private var subscribeService: SubscribeService,
    private var jwtUtil: JwtUtil
){

    private val log = LoggerFactory.getLogger(JwtUtil::class.java)

    @RequestMapping("addSubscribe")
    fun addSubscribe(@RequestBody subscribeDto: SubscribeDto, request: HttpServletRequest) : Boolean{

        println("subscribeDto = ${subscribeDto}")

        subscribeDto.subscriberUserSeq = jwtUtil.requestToUserSeq(request)
        val saveCheck: Boolean =  subscribeService.addSubscribe(subscribeDtoToEntity(subscribeDto))


        println("saveCheck = ${saveCheck}")

        return saveCheck;
    }

}