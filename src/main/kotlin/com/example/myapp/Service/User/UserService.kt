package com.example.myapp.Service.User

import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.User


interface UserService {

    fun userJoin(map: User?)

    fun searchUser(userDtoToEntity: User): Long?

    fun getFindUserId(userDtoToEntity: User) : String

    fun userLogin(userDtoToEntity: User): UserDto?

    fun getUserInfo(userSeq: Long?): UserDto?

    fun insertRefreshToken(refreshToken: String, newRefreshToken: String, userSeq: Long?)


}