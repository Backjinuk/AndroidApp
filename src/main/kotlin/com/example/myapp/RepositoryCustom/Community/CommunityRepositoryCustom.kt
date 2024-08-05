package com.example.myapp.RepositoryCustom.Community

import com.example.myapp.Entity.Community
import org.springframework.stereotype.Repository

@Repository
interface CommunityRepositoryCustom {

    fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?): List<Community>
}
