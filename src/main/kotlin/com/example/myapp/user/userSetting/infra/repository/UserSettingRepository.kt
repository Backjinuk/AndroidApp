package com.example.myapp.user.userSetting.infra.repository

import com.example.myapp.user.userSetting.domain.entity.UserSettingEntity
import org.springframework.stereotype.Repository

@Repository
interface UserSettingRepository {

    fun createDefaultUserSetting(savedEntity: UserSettingEntity) : UserSettingEntity

    fun updateUserSettingByUserSetting(userSettingEntity: UserSettingEntity) : UserSettingEntity

    fun findUserSettingByUserSeq(updateUserSeq: Long) : UserSettingEntity
}