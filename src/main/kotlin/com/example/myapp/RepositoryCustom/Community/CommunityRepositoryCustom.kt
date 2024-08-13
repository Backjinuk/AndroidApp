package com.example.myapp.RepositoryCustom.Community

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Entity.Community
import com.example.myapp.Entity.CommunityApply
import org.springframework.stereotype.Repository

interface CommunityRepositoryCustom {

    fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?): List<CommunityDto>
    fun updateCommunityUserTotal(commuSeq: Long)


}
