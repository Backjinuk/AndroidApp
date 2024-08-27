package com.example.myapp.RepositoryCustom.Subscribe

import com.example.myapp.Dto.SubscribeDto
import com.example.myapp.Entity.QSubscribe
import com.example.myapp.Entity.QUser
import com.example.myapp.Entity.Subscribe
import com.example.myapp.Util.ModelMapperUtil.Companion.subscribeEntityToDto
import com.example.myapp.Util.ModelMapperUtil.Companion.userEntityToDto
import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class SubscribeRepositoryCustomImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : SubscribeRepositoryCustom {

    private val qSubscribe: QSubscribe = QSubscribe.subscribe
    private val qUser: QUser = QUser.user

    @Transactional
    override fun addSubscribe(subscribe: Subscribe): Boolean {
        val exists = queryFactory
            .selectFrom(qSubscribe)
            .where(
                qSubscribe.subscriberUserSeq.eq(subscribe.subscriberUserSeq)
                    .and(qSubscribe.subscriberOwnerUserSeq.eq(subscribe.subscriberOwnerUserSeq))
            )
            .fetchCount() > 0

        if (!exists) {
            entityManager.persist(subscribe)
        } else {
            val existingSubscribe = queryFactory
                .selectFrom(qSubscribe)
                .where(
                    qSubscribe.subscriberUserSeq.eq(subscribe.subscriberUserSeq)
                        .and(qSubscribe.subscriberOwnerUserSeq.eq(subscribe.subscriberOwnerUserSeq))
                )
                .fetchFirst()

            // Update the subscribe status based on the current status
            val newStatus = if (existingSubscribe?.subscribeStatus == 'Y') 'N' else 'Y'

            queryFactory.update(qSubscribe)
                .set(qSubscribe.subscribeStatus, newStatus)
                .where(
                    qSubscribe.subscriberUserSeq.eq(subscribe.subscriberUserSeq)
                        .and(qSubscribe.subscriberOwnerUserSeq.eq(subscribe.subscriberOwnerUserSeq))
                )
                .execute()
        }

        return exists
    }

    override fun getSubscribe(subscribe: Subscribe): Subscribe? {
        return queryFactory
            .selectFrom(qSubscribe)
            .where(
                qSubscribe.subscriberUserSeq.eq(subscribe.subscriberUserSeq)
                    .and(qSubscribe.subscriberOwnerUserSeq.eq(subscribe.subscriberOwnerUserSeq))
            )
            .fetchFirst()
    }

    override fun getSubscribeList(subscribe: Subscribe): List<SubscribeDto> {

        val query = queryFactory
            .select(qSubscribe, qUser)
            .from(qSubscribe)
            .join(qUser).on(qUser.userSeq.eq(qSubscribe.subscriberUserSeq))
            .where(qSubscribe.subscriberUserSeq.eq(subscribe.subscriberUserSeq))
            .fetch()
            .map { tuple: Tuple ->
                val subscribeEntity = tuple.get(qSubscribe)
                val userEntity = tuple.get(qUser)
                val subscribeDto = subscribeEntity?.let { subscribeEntityToDto(it) }
                subscribeDto?.subscribeUser = userEntity?.let { userEntityToDto(it) }
                subscribeDto
            }

        return query as List<SubscribeDto>
    }
}
