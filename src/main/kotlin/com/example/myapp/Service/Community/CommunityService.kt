package com.example.myapp.Service.Community

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Entity.Community
import org.springframework.stereotype.Service

@Service
interface CommunityService {

    fun addCommunity(commuDtoToEntity: Community): Community;

    fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?, userSeq: Long): List<CommunityDto>

    fun updateCommunityUserTotal(commuSeq: Long)

    fun getCommunityInfo(communityDTO: CommunityDto): MutableMap<String, Any>


}