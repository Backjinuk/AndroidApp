package com.example.myapp.user.userProfile.infra.repository

import com.example.myapp.user.userProfile.domain.entity.QUserProfileEntity
import com.example.myapp.user.userProfile.domain.entity.UserProfileEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class UserProfileRepositoryImpl(
    private val entityManager: EntityManager,
    private val queryFactory : JPAQueryFactory
) : UserProfileRepository {

    private val qUserProfile : QUserProfileEntity = QUserProfileEntity.userProfileEntity

    @Transactional
    override fun userProfitableSetting(userProfile: UserProfileEntity): UserProfileEntity {
        entityManager.persist(userProfile)
        return userProfile
    }
}