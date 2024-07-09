package com.example.myapp.Service

import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.User
import com.example.myapp.Repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


interface UserService {

    fun userJoin(map: User?)

    fun searchUser(userDtoToEntity: User): Long?

    fun getFindUserId(userDtoToEntity: User) : String

    fun userLogin(userDtoToEntity: User): UserDto?


}