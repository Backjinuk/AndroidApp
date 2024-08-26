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
    fun getSubscribeList(request: HttpServletRequest): MutableMap<String, List<SubscribeDto>> {
        // 구독 정보를 담을 DTO를 생성합니다.
        val subscribeDto = SubscribeDto()

        // JWT를 통해 사용자 ID를 설정합니다.
        subscribeDto.subscriberUserSeq = jwtUtil.requestToUserSeq(request)

        // 구독 리스트를 가져옵니다.
        val subscribeDtoList: List<SubscribeDto> = subscribeService.getSubscribeList(subscribeDtoToEntity(subscribeDto))

        // Map을 초기화하고 데이터를 설정합니다.
        val mutableMap: MutableMap<String, List<SubscribeDto>> = mutableMapOf(
            "subscriberDtoList" to subscribeDtoList // 'to' 키워드를 사용해 Map에 키-값 쌍을 추가합니다.
        )

        return mutableMap
    }



}