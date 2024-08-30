package com.example.myapp.Controller

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Service.CommunityApply.CommunityApplyService
import com.example.myapp.Util.JwtUtil
import com.example.myapp.Util.ModelMapperUtil.Companion.communityApplyDtoToEntity
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CommunityApplyControllerTest @Autowired constructor(
    private val communityApplyService: CommunityApplyService,
    private val jwtUtil: JwtUtil
){

    @Test
    fun getCommunityApplyList() {

        val communityApplyDto : CommunityApplyDto = CommunityApplyDto();
        communityApplyDto.applyUserSeq = 4

        val communityApplyDtoList : List<CommunityApplyDto> = communityApplyService.getCommunityApplyList(communityApplyDtoToEntity(communityApplyDto));

        println("communityApplyDtoList.size = ${communityApplyDtoList.size}")

        for (communityApplyDto in communityApplyDtoList){
            println("communityApplyDto.applyUserSeq = ${communityApplyDto.applyUserSeq}")
            println("communityApplyDto.communityApplySeq = ${communityApplyDto.communityApplySeq}")
            println("communityApplyDto.applyCommuSeq = ${communityApplyDto.applyCommuSeq}")
        }

    }
}