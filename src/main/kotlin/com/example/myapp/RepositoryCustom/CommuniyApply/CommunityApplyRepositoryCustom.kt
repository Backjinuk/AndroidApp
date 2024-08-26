package com.example.myapp.RepositoryCustom.CommuniyApply

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Entity.CommunityApply


interface CommunityApplyRepositoryCustom {

    fun getCommunityApplyList(communityApply: CommunityApply): List<CommunityApplyDto>
}