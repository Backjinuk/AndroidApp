package com.example.myapp.RepositoryCustom

import com.example.myapp.Entity.User
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.count
import com.linecorp.kotlinjdsl.selectQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository


@Repository
class UserRepositoryCustomImpl @Autowired constructor(
    @PersistenceContext private val entityManager: EntityManager
) : UserRepositoryCustom {


    override fun findByUserId(userId: String, queryFactory: QueryFactoryImpl): User? {
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

    override fun userLogin(user: User, queryFactory: QueryFactoryImpl): Long? {
        val query = queryFactory.selectQuery<Long> {
            select(count(User::userId))
            from(entity(User::class))
            where(
                and(
                    col(User::userId).equal(user.userId),
                    col(User::passwd).equal(user.passwd)
                )
            )
        }
        return query.resultList.firstOrNull()
    }


}