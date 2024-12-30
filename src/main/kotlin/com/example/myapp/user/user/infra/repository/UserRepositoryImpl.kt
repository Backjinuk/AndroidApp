package com.example.myapp.user.user.infra.repository

import com.example.myapp.user.user.domain.entity.QUserEntity
import com.example.myapp.user.user.domain.entity.UserEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : UserRepository {

    private val qUserEntity : QUserEntity = QUserEntity.userEntity

    override fun userIsExistsByEmail(email: String): Boolean {
        var result = queryFactory.select(qUserEntity.email)
            .from(qUserEntity)
            .where(qUserEntity.email.eq(email))
            .fetchFirst();

        return result != null
    }

    override fun userJoin(user: UserEntity): UserEntity {
        entityManager.persist(user)
        return user
    }
}