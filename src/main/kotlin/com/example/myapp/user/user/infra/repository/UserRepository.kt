package com.example.myapp.user.user.infra.repository

import com.example.myapp.user.user.domain.entity.UserEntity
import org.springframework.stereotype.Repository

@Repository
interface UserRepository {

    fun userIsExistsByEmail(email: String): Boolean

    fun userJoin(user : UserEntity): UserEntity
}
