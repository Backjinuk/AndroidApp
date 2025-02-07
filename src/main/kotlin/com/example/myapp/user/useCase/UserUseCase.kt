package com.example.myapp.user.useCase

import com.example.myapp.user.user.controller.dto.LoginResponseDto
import com.example.myapp.user.user.domain.dto.UserDto
import org.springframework.stereotype.Service

@Service
interface UserUseCase {

    // 회원가입 기능
    fun registerUser(userDto: UserDto): UserDto

    fun updateUserInfoByUser(userDto: UserDto): UserDto

    // 로그인 기능
    fun login(userDto: UserDto): LoginResponseDto
}