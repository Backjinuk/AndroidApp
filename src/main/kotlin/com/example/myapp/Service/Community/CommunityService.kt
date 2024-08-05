package com.example.myapp.Service.Community

import com.example.myapp.Entity.Community


interface CommunityService {

    fun addCommunity(commuDtoToEntity: Community): Community;

    fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?): List<Community>

}