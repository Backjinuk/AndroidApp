package com.example.myapp.RepositoryCustom

import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.User
import com.linecorp.kotlinjdsl.QueryFactoryImpl

import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryCustom {

    fun findByUserId(it: String, queryFactory: QueryFactoryImpl): User?

    fun userLogin(it: User, queryFactory: QueryFactoryImpl) : UserDto?

    fun searchUser(it: User, queryFactory: QueryFactoryImpl) : Long?

}