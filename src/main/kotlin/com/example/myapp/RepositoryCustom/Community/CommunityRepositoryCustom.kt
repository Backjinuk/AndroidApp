package com.example.myapp.RepositoryCustom.Community

import com.example.myapp.Dto.CommunityDto

interface CommunityRepositoryCustom {

    fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?, userSeq: Long): List<CommunityDto>

    fun updateCommunityUserTotal(commuSeq: Long)



}
