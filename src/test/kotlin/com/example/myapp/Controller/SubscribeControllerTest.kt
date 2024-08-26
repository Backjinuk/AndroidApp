package com.example.myapp.Controller

import com.example.myapp.Dto.SubscribeDto
import com.example.myapp.Service.Subscribe.SubscribeService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.subscribeDtoToEntity
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SubscribeControllerTest @Autowired constructor(
    private var subscribeService: SubscribeService,
    private var jwtUtil: JwtUtil
){
    private val log = LoggerFactory.getLogger(JwtUtil::class.java)

    @Test
    fun addSubscribe() {
    }

    @Test
    fun getSubscribe() {
    }

    @Test
    fun getSubscribeList( ){
        val subscribeDto: SubscribeDto = SubscribeDto();

        subscribeDto.subscriberUserSeq = 4

        val subscribeDtoList:List<SubscribeDto> = subscribeService.getSubscribeList(subscribeDtoToEntity(subscribeDto));

        for (subscribeDto in subscribeDtoList){
            println("subscribeDto.subscribeSeq = ${subscribeDto.subscribeSeq}")
            println("subscribeDto.subscriberUserSeq = ${subscribeDto.subscriberUserSeq}")
            println("subscribeDto.subscriberOwnerUserSeq = ${subscribeDto.subscriberOwnerUserSeq}")
            println("subscribeDto.subscribeStatus = ${subscribeDto.subscribeStatus}")
        }

    }

}