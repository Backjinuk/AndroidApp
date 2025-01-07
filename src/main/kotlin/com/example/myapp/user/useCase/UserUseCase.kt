package com.example.myapp.user.useCase

import com.example.myapp.user.user.domain.dto.UserDto
import org.springframework.stereotype.Service

@Service
interface UserUseCase {

    fun registerUser(userDto: UserDto): UserDto

}