package com.example.myapp.RepositoryCustom

import com.linecorp.kotlinjdsl.QueryFactoryImpl
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import org.springframework.stereotype.Repository

@Repository
interface UserRepositoryCustom {

    fun findByUserId(it: String, queryFactory: QueryFactoryImpl): com.example.myapp.Entity.User?

}