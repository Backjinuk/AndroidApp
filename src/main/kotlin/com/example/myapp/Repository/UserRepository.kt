package com.example.myapp.Repository

import com.example.myapp.Entity.User
import com.example.myapp.Service.UserService
import com.example.myapp.Service.impl.UserServiceImpl
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {



}