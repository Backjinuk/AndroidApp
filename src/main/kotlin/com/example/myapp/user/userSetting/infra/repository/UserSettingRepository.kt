package com.example.myapp.user.userSetting.infra.repository

import com.example.myapp.user.userSetting.domain.entity.UserSettingEntity

interface UserSettingRepository {

    fun userSettingTableSetting(savedEntity: UserSettingEntity) : UserSettingEntity

}