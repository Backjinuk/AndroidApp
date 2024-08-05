package com.example.myapp.Service.User.impl

import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.User
import com.example.myapp.Repository.User.UserRepository
import com.example.myapp.Service.User.UserService
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

    override fun searchUser(user: User): Long? {
        return user?.let { userRepository.searchUser(it) }
    }
    
    override fun getFindUserId(user: User): String {
        val findUser = user.userId?.let { userRepository.findByUserId(it) }
        return findUser?.toString() ?: "N"
    }
    
    override fun userLogin(user: User): UserDto? {
        return user?.let{userRepository.userLogin(it)}
    }

    override fun getUserInfo(userSeq: Long?): UserDto? {
        return userRepository.getUserInfo(userSeq)
    }

    override fun insertRefreshToken(refreshToken: String, newRefreshToken: String, userSeq: Long?) {
        return userRepository.insertRefreshToken(refreshToken, newRefreshToken,  userSeq)
    }

}

