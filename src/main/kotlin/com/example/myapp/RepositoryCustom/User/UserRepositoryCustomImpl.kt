package com.example.myapp.RepositoryCustom.User

import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.QUser.user
import com.example.myapp.Entity.QUserToken.userToken
import com.example.myapp.Entity.User
import com.example.myapp.Entity.UserToken
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.modelmapper.ModelMapper
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserRepositoryCustomImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : UserRepositoryCustom {

    private var modelMapper = ModelMapper()

    override fun findByUserId(userId: String): User? {
        return queryFactory
            .selectFrom(user)
            .where(user.userId.eq(userId))
            .fetchFirst()
    }

    override fun userLogin(userParam: User): UserDto? {
        val result = queryFactory
            .selectFrom(user)
            .where(
                user.userId.eq(userParam.userId),
                user.passwd.eq(userParam.passwd)
            )
            .fetchFirst()

        return result?.let { modelMapper.map(it, UserDto::class.java) }
    }

    override fun searchUser(it: User): Long? {
        return queryFactory
            .select(user.count())
            .from(user)
            .where(user.userId.eq(it.userId))
            .fetchFirst()
    }

    override fun getUserInfo(userSeq: Long?): UserDto? {
        val result = queryFactory
            .selectFrom(user)
            .where(user.userSeq.eq(userSeq))
            .fetchFirst()

        return result?.let { modelMapper.map(it, UserDto::class.java) }
    }

    @Transactional
    override fun insertRefreshToken(refreshToken: String, newRefreshToken: String, userSeq: Long?) {
        val tokenExists = queryFactory
            .selectFrom(userToken)
            .where(userToken.refreshUserToken.eq(refreshToken))
            .fetchCount() > 0

        if (!tokenExists) {
            var existingUserToken: UserToken? = null

            if (refreshToken.isNotEmpty()) {
                existingUserToken = queryFactory
                    .selectFrom(userToken)
                    .where(userToken.user.userSeq.eq(userSeq))
                    .fetchFirst()
            }

            if (existingUserToken == null) {
                val userToken = UserToken().apply {
                    this.refreshUserToken = refreshToken
                    this.user = userSeq?.let { User().apply { this.userSeq = it } }
                }
                entityManager.persist(userToken)
            } else {
                queryFactory.update(userToken)
                    .set(userToken.refreshUserToken, newRefreshToken)
                    .where(userToken.user.userSeq.eq(userSeq))
                    .execute()
            }
        } else {
            queryFactory.update(userToken)
                .set(userToken.refreshUserToken, newRefreshToken)
                .where(userToken.refreshUserToken.eq(refreshToken))
                .execute()
        }
    }

    override fun refreshTokenFindUserInfo(refreshToken: String?): UserDto {
        val userTokenResult = queryFactory
            .selectFrom(userToken)
            .where(userToken.refreshUserToken.eq(refreshToken))
            .fetchFirst()

        val userResult = userTokenResult?.user?.userSeq?.let {
            queryFactory
                .selectFrom(user)
                .where(user.userSeq.eq(it))
                .fetchFirst()
        }

        return userResult?.let { modelMapper.map(it, UserDto::class.java) }!!
    }
}
