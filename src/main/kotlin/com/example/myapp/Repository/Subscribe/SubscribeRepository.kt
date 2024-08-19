package com.example.myapp.Repository.Subscribe

import com.example.myapp.Entity.Subscribe
import com.example.myapp.RepositoryCustom.Subscribe.SubscribeRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubscribeRepository : JpaRepository<Subscribe, Long>, SubscribeRepositoryCustom {

}