package com.example.myapp.Service.impl

import com.example.myapp.Entity.User
import com.example.myapp.Repository.UserRepository
import com.example.myapp.Service.UserService
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val entityManager: EntityManager,
    private val queryFactory: QueryFactoryImpl
) : UserService {

    override fun userJoin(user: User?) {
        /*for instance, to save the user to the database*/
        //userRepository?.userJoin(user)

        user?.let { userRepository.save(it) }
    }

    override fun getFindUserId(user: User): String {
        val findUser = user.userId?.let { userRepository.findByUserId(it, queryFactory) }
        return findUser?.toString() ?: "User not found"
    }
}