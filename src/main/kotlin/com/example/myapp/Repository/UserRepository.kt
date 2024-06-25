package com.example.myapp.Repository

import com.example.myapp.Entity.User
import com.example.myapp.RepositoryCustom.UserRepositoryCustom
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.selectQuery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom {




}