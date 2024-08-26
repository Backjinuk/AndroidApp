package com.example.myapp.Service.CommunityApply

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Entity.CommunityApply

interface CommunityApplyService {
    fun getCommunityApplyList(communityApply: CommunityApply): List<CommunityApplyDto>

}