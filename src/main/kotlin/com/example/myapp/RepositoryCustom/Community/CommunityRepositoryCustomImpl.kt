package com.example.myapp.RepositoryCustom.Community

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Entity.Community
import com.example.myapp.Entity.QCommunity
import com.example.myapp.Entity.QCommunityApply
import com.example.myapp.Util.ModelMapperUtil.Companion.commuEntityToDto
import com.example.myapp.Util.ModelMapperUtil.Companion.userDtoToEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository

@Repository
class CommunityRepositoryCustomImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : CommunityRepositoryCustom {

    val qCommunity: QCommunity = QCommunity.community
    val qCommunityApply: QCommunityApply = QCommunityApply.communityApply

    override fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?, userSeq: Long): List<CommunityDto> {
        if (latitude == null || longitude == null || radius == null) {
            throw IllegalArgumentException("Latitude, Longitude, and Radius must be provided.")
        }

        val query = """
            SELECT c
            FROM Community c
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

        // 쿼리 실행 후 결과를 Community 타입으로 직접 변환
        val resultList = entityManager.createQuery(query, Community::class.java)
            .setParameter("latitude", latitude)
            .setParameter("longitude", longitude)
            .setParameter("radius", radius)
            .resultList

        // 결과 리스트를 DTO로 변환
        return resultList.map { community ->
            commuEntityToDto(community)
        }
    }

    @Transactional
    override fun updateCommunityUserTotal(commuSeq: Long) {
        queryFactory
            .update(qCommunity)
            .set(qCommunity.userCount, qCommunity.userCount.add(1))
            .where(qCommunity.commuSeq.eq(commuSeq))
            .execute()
    }


    override fun getCommunityInfo(communityDTO: CommunityDto): MutableMap<String, Any> {
        return queryFactory
            .select(qCommunity, qCommunityApply.applyStatus)
            .from(qCommunity)
            .join(qCommunityApply).on(qCommunity.commuSeq.eq(qCommunityApply.applyCommuSeq))
            .where(
                qCommunity.commuSeq.eq(communityDTO.commuSeq)
                    .and(qCommunity.commuWrite.eq(userDtoToEntity(communityDTO.commuWrite)))
            )
            .fetchOne()
            ?.let { tuple ->
                mutableMapOf<String, Any>(
                    "community" to tuple.get(qCommunity) as Any,
                    "applyStatus" to tuple.get(qCommunityApply.applyStatus) as Any
                )
            } ?: mutableMapOf()
    }

}
