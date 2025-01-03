package com.example.myapp.user.userSetting.service

import com.example.myapp.user.userSetting.domain.dto.UserSettingDto
import com.example.myapp.user.userSetting.domain.entity.UserSettingEntity
import com.example.myapp.user.userSetting.infra.repository.UserSettingRepository
import jakarta.validation.Validator
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class UserSettingService(
    private val userSettingRepository: UserSettingRepository,
    private val modelMapper: ModelMapper,
    private val validator: Validator
) {

    fun userSettingTableSetting(userSettingDto: UserSettingDto) : UserSettingDto {
        val violations = validator.validate(userSettingDto)
        if (violations.isNotEmpty()) {
            // 예외 처리 로직
            throw IllegalArgumentException(
                "유효성 검증 실패: " + violations.joinToString { it.message }
            )
        }

        val mappedEntity = modelMapper.map(userSettingDto, UserSettingEntity::class.java)
        val returnValue = userSettingRepository.userSettingTableSetting(mappedEntity)
        return modelMapper.map(returnValue, UserSettingDto::class.java)
    }
}