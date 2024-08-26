package com.example.myapp.Controller

import com.example.myapp.Dto.SubscribeDto
import com.example.myapp.Service.Subscribe.SubscribeService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.subscribeDtoToEntity
import com.example.myapp.Util.ModelMapperUtil.Companion.subscribeEntityToDto
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
        subscribeDto.subscriberUserSeq = jwtUtil.requestToUserSeq(request)
        val saveCheck: Boolean =  subscribeService.addSubscribe(subscribeDtoToEntity(subscribeDto))
        return saveCheck;
    }

    @RequestMapping("getSubscribe")
    fun getSubscribe(@RequestBody subscribeDto: SubscribeDto, request: HttpServletRequest) : SubscribeDto? {
        subscribeDto.subscriberUserSeq = jwtUtil.requestToUserSeq(request)

        val subscribe = subscribeService.getSubscribe(subscribeDtoToEntity(subscribeDto))

        return subscribe?.let { subscribeEntityToDto(it) };

    }

    @RequestMapping("getSubscribeList")
    fun getSubscribeList(@RequestBody subscribeDto: SubscribeDto, request: HttpServletRequest) : MutableMap<String, List<SubscribeDto>>? {
        val mutableMap: MutableMap<String, List<SubscribeDto>>? = null;
        subscribeDto.subscriberUserSeq = jwtUtil.requestToUserSeq(request)

        val subscribeDtoList:List<SubscribeDto> = subscribeService.getSubscribeList(subscribeDtoToEntity(subscribeDto));

        mutableMap?.set("subscriberDtoList", subscribeDtoList);

        return mutableMap;
    }



}