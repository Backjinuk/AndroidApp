package com.example.myapp.Service.Community

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Entity.Community
import com.example.myapp.Entity.CommunityApply
import org.springframework.stereotype.Service

@Service
interface CommunityService {

    fun addCommunity(commuDtoToEntity: Community): Community;

    fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?): List<CommunityDto>

    fun addCommunityApply(commuApply: CommunityApply)

    fun updateCommunityUserTotal(commuSeq: Long)

}