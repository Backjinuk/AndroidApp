package com.example.myapp.RepositoryCustom.Community

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Entity.Community
import com.example.myapp.Entity.QCommunity
import com.example.myapp.Util.ModelMapperUtil.Companion.commuEntityToDto
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class CommunityRepositoryCustomImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : CommunityRepositoryCustom {

    override fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?, userSeq: Long): List<CommunityDto> {
        if (latitude == null || longitude == null || radius == null) {
            throw IllegalArgumentException("Latitude, Longitude, and Radius must be provided.")
        }

        val query = """
            SELECT c, 
            COALESCE(
                (SELECT ca.applyStatus 
                 FROM CommunityApply ca 
                 WHERE ca.applyUserSeq = :userSeq AND ca.applyCommuSeq = c.commuSeq
                ), 'N') AS applyStatus
            FROM Community c
            LEFT JOIN CommunityApply ca ON ca.applyCommuSeq = c.commuSeq AND ca.applyUserSeq = :userSeq
            WHERE (6371 * acos(
                    cos(radians(:latitude)) * cos(radians(c.latitude)) *
                    cos(radians(c.longitude) - radians(:longitude)) +
                    sin(radians(:latitude)) * sin(radians(c.latitude))
            )) <= :radius
            ORDER BY (6371 * acos(
                cos(radians(:latitude)) * cos(radians(c.latitude)) *
                cos(radians(c.longitude) - radians(:longitude)) +
                sin(radians(:latitude)) * sin(radians(c.latitude))
            ))
        """

        val resultList = entityManager.createQuery(query)
            .setParameter("latitude", latitude)
            .setParameter("longitude", longitude)
            .setParameter("radius", radius)
            .setParameter("userSeq", userSeq)
            .resultList

        return resultList.map { result ->
            val array = result as Array<*>
            val community = array[0] as Community
            val applyStatus = array[1] as Char? // Char로 캐스팅
            commuEntityToDto(community, applyStatus)
        }.toList()
    }

    @Transactional
    override fun updateCommunityUserTotal(commuSeq: Long) {
        val qCommunity = QCommunity.community

        queryFactory
            .update(qCommunity)
            .set(qCommunity.userCount, qCommunity.userCount.add(1))
            .where(qCommunity.commuSeq.eq(commuSeq))
            .execute()
    }
}
