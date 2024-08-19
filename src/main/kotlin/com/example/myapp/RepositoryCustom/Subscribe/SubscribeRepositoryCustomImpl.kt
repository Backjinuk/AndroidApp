package com.example.myapp.RepositoryCustom.Subscribe

import com.example.myapp.Entity.Subscribe
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.count
import com.linecorp.kotlinjdsl.selectQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class SubscribeRepositoryCustomImpl @Autowired constructor(
    @PersistenceContext private val entityManager: EntityManager,
    private val queryFactory: QueryFactoryImpl
) :  SubscribeRepositoryCustom {


    @Transactional
    override fun addSubscribe(subscribe: Subscribe): Boolean {
        val query = queryFactory.selectQuery<Long> {
            select(count(Subscribe::subscribeSeq))
            from(entity(Subscribe::class))
            where(
                    col(Subscribe::subscriberUserSeq).equal(subscribe.subscriberUserSeq)
            )
        }.singleResult > 0

        if(!query){
            entityManager.persist(subscribe)
        }



        return query;
    }
}