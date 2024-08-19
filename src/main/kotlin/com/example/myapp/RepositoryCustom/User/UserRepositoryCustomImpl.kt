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
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Join
import jakarta.persistence.criteria.Root
import jakarta.transaction.Transactional
import kotlinx.coroutines.selects.select
import org.aspectj.weaver.tools.cache.SimpleCacheFactory.path
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.JpaSort.path
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
            var existingUserToken :UserToken ?= UserToken();


            if(refreshToken != ""){

                existingUserToken  = entityManager.createQuery(
                    entityManager.criteriaBuilder.createQuery(UserToken::class.java).apply {
                        val root: Root<UserToken> = from(UserToken::class.java)
                        val userJoin: Join<UserToken, User> = root.join("user")
                        select(root).where(entityManager.criteriaBuilder.equal(userJoin.get<Long>("userSeq"), userSeq))
                    }
                ).resultList.firstOrNull()

                println("userToken?.userTokenSeq = ${existingUserToken?.userTokenSeq}")
            }

            if(existingUserToken == null){
                val userToken = UserToken().apply {
                    this.refreshUserToken = refreshToken
                    this.user = userSeq?.let { User().apply { this.userSeq = it } }
                }
                entityManager.persist(userToken)
            }else{
                entityManager.createQuery(
                    entityManager.criteriaBuilder.createCriteriaUpdate(UserToken::class.java).apply {
                        val root = from(UserToken::class.java)
                        val userJoin = root.join<UserToken, User>("user")
                        set(root.get("refreshUserToken"), newRefreshToken)
                        where(entityManager.criteriaBuilder.equal(userJoin.get<Long>("userSeq"), userSeq))
                    }
                ).executeUpdate()
            }
        }else{
            queryFactory.updateQuery<UserToken> {
                set(col(UserToken::refreshUserToken), newRefreshToken)
                where(col(UserToken::refreshUserToken).equal(refreshToken))
            }.executeUpdate()
        }

    }

    override fun refreshTokenFindUserInfo(refreshToken: String?): UserDto {

         val query =  queryFactory.selectQuery<UserToken>{
             select(entity(UserToken::class))
             from(entity(UserToken::class))
             where(col(UserToken::refreshUserToken).equal(refreshToken))
         }

        val userToken: UserToken? = query.resultList.firstOrNull();

        val query2 = queryFactory.selectQuery<User> {
            select(entity(User::class))
            from(entity(User::class))
            where(
                and(
                    col(User::userSeq).equal(userToken?.user?.userSeq?.toLong())
                )
            )
        }

        return modelMapper.map(query2.resultList.firstOrNull(), UserDto::class.java)

    }




}