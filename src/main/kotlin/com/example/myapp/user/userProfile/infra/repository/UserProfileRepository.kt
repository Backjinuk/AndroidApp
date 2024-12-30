package com.example.myapp.user.userProfile.infra.repository

import com.example.myapp.user.userProfile.domain.entity.UserProfileEntity
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository {

    fun userProfitableSetting(userProfile: UserProfileEntity): UserProfileEntity

}