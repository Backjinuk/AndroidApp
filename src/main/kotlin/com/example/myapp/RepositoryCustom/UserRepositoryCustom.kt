package com.example.myapp.RepositoryCustom

import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.User
import com.linecorp.kotlinjdsl.QueryFactoryImpl

import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryCustom {

    fun findByUserId(it: String): User?

    fun userLogin(it: User) : UserDto?

    fun searchUser(it: User) : Long?

    fun getUserInfo(userSeq: Long?): UserDto?

    fun insertRefreshToken(refreshToken: String, userSeq: Long?)

}