package com.example.myapp.Repository.Community

import com.example.myapp.Entity.CommunityApply
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommunityApplyRepository : JpaRepository<CommunityApply, Long> {
}