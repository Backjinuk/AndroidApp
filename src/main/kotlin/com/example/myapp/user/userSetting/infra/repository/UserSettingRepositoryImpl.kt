package com.example.myapp.user.userSetting.infra.repository

import com.example.myapp.user.user.infra.repository.UserRepository
import com.example.myapp.user.userSetting.domain.entity.UserSettingEntity
import org.springframework.stereotype.Repository

@Repository
class UserSettingRepositoryImpl ( ) : UserSettingRepository {


    override fun userSettingTableSetting(savedEntity: UserSettingEntity): UserSettingEntity {
        TODO("Not yet implemented")
    }


}