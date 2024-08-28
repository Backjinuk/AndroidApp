package com.example.myapp.RepositoryCustom.CommuniyApply

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Entity.CommunityApply


interface CommunityApplyRepositoryCustom {

    fun getCommunityApplyList(communityApply: CommunityApply): List<CommunityApplyDto>

    fun getCommunityApplyUser(communityApplyDtoToEntity: CommunityApply): CommunityApplyDto

    fun addCommunityApply(commuApply: CommunityApply): Boolean
}