package com.example.myapp.user.user.service

import com.example.myapp.user.user.domain.dto.UserDto
import com.example.myapp.user.user.domain.dto.UserTokenDto
import com.example.myapp.user.user.domain.entity.UserEntity
import com.example.myapp.user.user.domain.entity.UserTokenEntity
import com.example.myapp.user.user.infra.repository.UserRepository
import jakarta.validation.Validator
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val modelMapper: ModelMapper,
    private val validator: Validator,
) {

    fun registerUser(userDto: UserDto): UserDto {
        val violations = validator.validate(userDto)
        if (violations.isNotEmpty()) {
            // 예외 처리 로직
            throw IllegalArgumentException(
                "유효성 검증 실패: " + violations.joinToString { it.message }
            )
        }

        val userEntity = userRepository.userJoin(modelMapper.map(userDto, UserEntity::class.java))
        return modelMapper.map(userEntity, UserDto::class.java)
    }

    fun userIsExistsByEmail(userDto: UserDto): Boolean {
        return userRepository.userIsExistsByEmail(userDto.email)
    }

    fun addUserTokenByUserSeq(userTokenDto: UserTokenDto): UserTokenDto {
        val validator = validator.validate(userTokenDto)

        if (validator.isNotEmpty()) {
            throw IllegalArgumentException(validator.joinToString { it.message })
        }

        val userTokenEntity =
            userRepository.addUserTokenByUserSeq(modelMapper.map(userTokenDto, UserTokenEntity::class.java))
        return modelMapper.map(userTokenEntity, UserTokenDto::class.java)
    }


}