package com.example.myapp.Repository.Community

import com.example.myapp.Entity.Community
import com.example.myapp.RepositoryCustom.Community.CommunityRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommunityRepository : JpaRepository<Community, Long>, CommunityRepositoryCustom{


}