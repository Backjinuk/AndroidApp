package com.example.myapp.Service.Community.Impl

import com.example.myapp.Entity.Community
import com.example.myapp.Repository.Community.CommunityRepository
import com.example.myapp.Service.Community.CommunityService
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service

@Service
class CommunityServiceImpl(
    private var communityRepository: CommunityRepository
) : CommunityService {

    override fun addCommunity(community: Community): Community {
        return communityRepository.save(community);
    }

    override fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?) : List<Community> {
        return communityRepository.getLocationBaseInquey(latitude, longitude, radius)
    }

}