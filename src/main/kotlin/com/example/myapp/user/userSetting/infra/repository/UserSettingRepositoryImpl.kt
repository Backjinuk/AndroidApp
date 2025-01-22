package com.example.myapp.user.userSetting.infra.repository

import com.example.myapp.user.userSetting.domain.entity.QUserSettingEntity
import com.example.myapp.user.userSetting.domain.entity.UserSettingEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class UserSettingRepositoryImpl (
    private val entityManager: EntityManager,
    private val queryFactory : JPAQueryFactory
) : UserSettingRepository {

    private val qUserSettingEntity : QUserSettingEntity = QUserSettingEntity.userSettingEntity

    override fun createDefaultUserSetting(savedEntity: UserSettingEntity): UserSettingEntity {
        entityManager.persist(savedEntity)
        return savedEntity
    }

    override fun updateUserSettingByUserSetting(userSettingEntity: UserSettingEntity): UserSettingEntity {
        TODO("Not yet implemented")
    }

    override fun findUserSettingByUserSeq(updateUserSeq: Long): UserSettingEntity {
        TODO("Not yet implemented")
    }


}