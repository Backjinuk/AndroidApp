package com.example.myapp.Controller

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Entity.CommunityApply
import com.example.myapp.Service.Community.CommunityService
import com.example.myapp.Service.CommunityApply.CommunityApplyService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.communityApplyDtoToEntity
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/communityApply/")
class CommunityApplyController @Autowired constructor(
    private val communityApplyService: CommunityApplyService,
    private var communityService: CommunityService,
    private val jwtUtil: JwtUtil
) {

    @RequestMapping("getCommuApplyUser")
    fun getCommuApplyUser(@RequestBody communityDTO: CommunityDto, request: HttpServletRequest) : MutableMap<String, CommunityApplyDto>{
        var communityApplyDto : CommunityApplyDto = CommunityApplyDto();
        communityApplyDto.applyUserSeq = jwtUtil.JwtTokenGetUserSeq(mapOf("AccessToken" to request.getHeader("AccessToken")))
        communityApplyDto.applyCommuSeq = communityDTO.commuSeq;

        communityApplyDto = communityApplyService.getCommuApplyUser(communityApplyDtoToEntity(communityApplyDto))

        println("communityApplyDto = ${communityApplyDto.applyUserSeq}")

        return mutableMapOf("applyStatus" to communityApplyDto)
    }


    @RequestMapping("commuApplyUser")
    fun commuApplyUser(@RequestBody communityDTO: CommunityDto, request:HttpServletRequest) : MutableMap<String, Boolean> {

        val commuApply : CommunityApply = CommunityApply();

        commuApply.applyUserSeq = jwtUtil.JwtTokenGetUserSeq(mapOf("AccessToken" to request.getHeader("AccessToken")))
        commuApply.applyCommuSeq = communityDTO.commuSeq;
        commuApply.applyStatus = 'Y';

        val applyChack : Boolean =  communityApplyService.addCommunityApply(commuApply)

        if(applyChack) communityService.updateCommunityUserTotal(communityDTO.commuSeq);

        return mutableMapOf("applyChack" to applyChack)

    }


}