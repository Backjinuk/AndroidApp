package com.example.myapp.RepositoryCustom.User

import com.example.myapp.Dto.UserDto
import com.example.myapp.Entity.User
import com.example.myapp.Entity.UserToken
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.count
import com.linecorp.kotlinjdsl.selectQuery
import com.linecorp.kotlinjdsl.updateQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class UserRepositoryCustomImpl @Autowired constructor(
    @PersistenceContext private val entityManager: EntityManager,
    private val queryFactory: QueryFactoryImpl
) : UserRepositoryCustom {

    private var modelMapper = ModelMapper();

    override fun findByUserId(userId: String): User? {
        val query = queryFactory.selectQuery<User> {
            select(entity(User::class))
            from(entity(User::class))
            where(
                and(
                    col(User::userId).equal(userId)
                )
            )
        }
        return query.resultList.firstOrNull()
    }

    override fun userLogin(user: User): UserDto? {
        val query = queryFactory.selectQuery<User> {
            select(entity(User::class))
            from(entity(User::class))
            where(
                and(
                    col(User::userId).equal(user.userId),
                    col(User::passwd).equal(user.passwd)
                )
            )
        }
        return modelMapper.map(query.resultList.firstOrNull(), UserDto::class.java)
    }

    override fun searchUser(it: User): Long? {
        val query = queryFactory.selectQuery<Long> {
            select(count(User::userId))
            from(entity(User::class))
            where(
                and(
                    col(User::userId).equal(it.userId)
                )
            )
        }
        return query.resultList.firstOrNull()
    }

    override fun getUserInfo(userSeq: Long?): UserDto? {
        val query = queryFactory.selectQuery<User> {
            select(entity(User::class))
            from(entity(User::class))
            where(
                and(
                    col(User::userSeq).equal(userSeq)
                )
            )
        }

        return modelMapper.map(query.resultList.firstOrNull(), UserDto::class.java)
    }

    @Transactional
    override fun insertRefreshToken(refreshToken: String, newRefreshToken: String, userSeq: Long?) {

        val query = queryFactory.selectQuery<Long> {
            select(count(entity(UserToken::class)))
            from(entity(UserToken::class))
            where(
                col(UserToken::refreshUserToken).equal(refreshToken)
            )
        }.singleResult > 0

        if(!query){

            val userToken = UserToken().apply {
                this.refreshUserToken = refreshToken
                this.user = userSeq?.let { User().apply { this.userSeq = it } }
            }

            println("userSeq = ${userSeq}")

            println("userToken = ${userToken.user?.userSeq}")

            entityManager.persist(userToken)
        }

        if(query){
            queryFactory.updateQuery<UserToken> {
                set(col(UserToken::refreshUserToken), newRefreshToken)
                where(col(UserToken::refreshUserToken).equal(refreshToken))
            }.executeUpdate()
        }


    }


}