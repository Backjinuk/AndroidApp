package com.example.myapp.Repository.User

import com.example.myapp.Entity.User
import com.example.myapp.RepositoryCustom.User.UserRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom {




}