package com.example.myapp.Service.Community.Impl

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Entity.Community
import com.example.myapp.Entity.CommunityApply
import com.example.myapp.Repository.Community.CommunityApplyRepository
import com.example.myapp.Repository.Community.CommunityRepository
import com.example.myapp.Service.Community.CommunityService
import org.springframework.stereotype.Service

@Service
class CommunityServiceImpl(
    private var communityRepository: CommunityRepository,
    private val communityApplyRepository: CommunityApplyRepository
) : CommunityService {

    override fun addCommunity(community: Community): Community {
        return communityRepository.save(community);
    }

    override fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?) : List<CommunityDto> {
        return communityRepository.getLocationBaseInquey(latitude, longitude, radius)
    }

    override fun addCommunityApply(commuApply: CommunityApply) {
        communityApplyRepository.save(commuApply)
    }

    override fun updateCommunityUserTotal(commuSeq: Long) {
        communityRepository.updateCommunityUserTotal(commuSeq)
    }

}