package com.example.myapp.RepositoryCustom.Subscribe

import com.example.myapp.Entity.Subscribe
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.count
import com.linecorp.kotlinjdsl.selectQuery
import com.linecorp.kotlinjdsl.updateQuery
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
        val exists = queryFactory.selectQuery<Long> {
            select(count(entity(Subscribe::class)))
            from(entity(Subscribe::class))
            where(
                col(Subscribe::subscriberUserSeq).equal(subscribe.subscriberUserSeq)
                    .and(col(Subscribe::subscriberOwnerUserSeq).equal(subscribe.subscriberOwnerUserSeq))
            )
        }.singleResult > 0

        if(!exists){
            entityManager.persist(subscribe)
        }else{

            val existingSubscribe = queryFactory.selectQuery<Subscribe> {
                select(entity(Subscribe::class))
                from(entity(Subscribe::class))
                where(
                    col(Subscribe::subscriberUserSeq).equal(subscribe.subscriberUserSeq)
                        .and(col(Subscribe::subscriberOwnerUserSeq).equal(subscribe.subscriberOwnerUserSeq))
                )
            }.resultList.firstOrNull()

            // Update the subscribe status based on the current status
            val newStatus = if (existingSubscribe?.subscribeStatus == 'Y') 'N' else 'Y'

            queryFactory.updateQuery<Subscribe> {
                set(col(Subscribe::subscribeStatus), newStatus)
                where(
                    col(Subscribe::subscriberUserSeq).equal(subscribe.subscriberUserSeq)
                        .and(col(Subscribe::subscriberOwnerUserSeq).equal(subscribe.subscriberOwnerUserSeq))
                )
            }.executeUpdate()

        }



        return exists;
    }

    override fun getSubscribe(subscribe: Subscribe): Subscribe? {
        return queryFactory.selectQuery<Subscribe> {
            select(entity(Subscribe::class))
            from(entity(Subscribe::class))
            where(
                col(Subscribe::subscriberUserSeq).equal(subscribe.subscriberUserSeq)
                    .and(col(Subscribe::subscriberOwnerUserSeq).equal(subscribe.subscriberOwnerUserSeq))
            )
        }.resultList.firstOrNull()
    }
}