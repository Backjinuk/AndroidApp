package com.example.myapp.user.userProfile.service

import com.example.myapp.user.userProfile.domain.dto.UserProfileDto
import com.example.myapp.user.userProfile.domain.entity.UserProfileEntity
import com.example.myapp.user.userProfile.infra.repository.UserProfileRepository
import jakarta.validation.Validator
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository,
    private val modelMapper: ModelMapper,
    private val validator: Validator

) {
    fun userProfitableSetting(userProfileDto: UserProfileDto): UserProfileDto? {

        val violations = validator.validate(userProfileDto)
        if (violations.isNotEmpty()) {
            // 예외 처리 로직
            throw IllegalArgumentException(
                "유효성 검증 실패: " + violations.joinToString { it.message }
            )
        }

        val returnValue = userProfileRepository.userProfitableSetting(modelMapper.map(userProfileDto, UserProfileEntity::class.java))
        return modelMapper.map(returnValue, UserProfileDto::class.java)
    }
}