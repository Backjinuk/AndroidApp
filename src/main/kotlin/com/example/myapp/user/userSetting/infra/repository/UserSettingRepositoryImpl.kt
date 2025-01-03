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

    override fun userSettingTableSetting(savedEntity: UserSettingEntity): UserSettingEntity {
        entityManager.persist(savedEntity)
        return savedEntity
    }


}