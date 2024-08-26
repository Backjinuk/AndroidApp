package com.example.myapp.RepositoryCustom.Community

import com.example.myapp.Dto.CommunityDto
import com.example.myapp.Entity.Community
import com.example.myapp.Util.ModelMapperUtil.Companion.commuEntityToDto
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Repository
class CommunityRepositoryCustomImpl(
    private val entityManager: EntityManager,
    private val queryFactory: QueryFactoryImpl
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
        val cb = entityManager.criteriaBuilder
        val update = cb.createCriteriaUpdate(Community::class.java)
        val root = update.from(Community::class.java)

        entityManager.createQuery(
            update
                .set(root.get<Int>("userCount"), cb.sum(root.get("userCount"), 1))
                .where(cb.equal(root.get<Long>("commuSeq"), commuSeq))
        ).executeUpdate()

    }



    // Haversine 거리 계산 함수
    private fun haversineDistance(latitude: Double, longitude: Double, latitude1: Double, longitude1: Double): Double {
        // 지구 반지름 (km)
        val earthRadius = 6371

        // 두 지점의 위도 차이를 라디안으로 변환
        val dLat = Math.toRadians(latitude1 - latitude)
        // 두 지점의 경도 차이를 라디안으로 변환
        val dLon = Math.toRadians(longitude1 - longitude)

        // Haversine 공식
        // sin^2((위도 차이) / 2)
        // cos(위도1) * cos(위도2) *
        // sin^2((경도 차이) / 2 )

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(latitude)) * cos(Math.toRadians(latitude1)) *
                sin(dLon / 2) * sin(dLon / 2)

        //Haversine 공식 최종 : 2 * atan2(√a, √(1-a))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // 최종 거리 계산 : 지구의 반지름 * c (단위 킬로미터)
        return earthRadius * c
    }
}