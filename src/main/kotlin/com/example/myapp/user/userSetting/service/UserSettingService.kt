package com.example.myapp.user.userSetting.service

import com.example.myapp.user.userSetting.domain.dto.UserSettingDto
import com.example.myapp.user.userSetting.domain.entity.UserSettingEntity
import com.example.myapp.user.userSetting.infra.repository.UserSettingRepository
import jakarta.validation.Validator
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class UserSettingService {

    private lateinit var userSettingRepository: UserSettingRepository

    private lateinit var modelMapper: ModelMapper

    private lateinit var validator: Validator

    fun userSettingTableSetting(userSettingDto: UserSettingDto) : UserSettingEntity {
        val violations = validator.validate(userSettingDto)
        if (violations.isNotEmpty()) {
            // 예외 처리 로직
            throw IllegalArgumentException(
                "유효성 검증 실패: " + violations.joinToString { it.message }
            )
        }

       val returnValue = userSettingRepository.userSettingTableSetting(modelMapper.map(userSettingDto, UserSettingEntity::class.java))
       return modelMapper.map(returnValue, UserSettingEntity::class.java)
    }
}