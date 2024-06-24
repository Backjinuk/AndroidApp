package com.example.myapp.Service.impl

import com.example.myapp.Entity.User
import com.example.myapp.Repository.UserRepository
import com.example.myapp.Service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun userJoin(user: User?) {
        /*for instance, to save the user to the database*/
        //userRepository?.userJoin(user)

        user?.let { userRepository.save(it) }
    }
}