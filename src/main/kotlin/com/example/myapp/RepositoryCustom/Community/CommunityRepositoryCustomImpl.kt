package com.example.myapp.RepositoryCustom.Community

import com.example.myapp.Entity.Community
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class CommunityRepositoryCustomImpl @Autowired constructor(
    @PersistenceContext private var entityManager: EntityManager,
    private var queryFactory: QueryFactoryImpl
) : CommunityRepositoryCustom {

    override fun getLocationBaseInquey(latitude: Double?, longitude: Double?, radius: Double?): List<Community> {
        if (latitude == null || longitude == null || radius == null) {
            throw IllegalArgumentException("Latitude, longitude, and radius must not be null")
        }

        val cb: CriteriaBuilder = entityManager.criteriaBuilder
        val cq: CriteriaQuery<Community> = cb.createQuery(Community::class.java)
        val root: Root<Community> = cq.from(Community::class.java)

        // Convert degrees to radians
        val latRad = cb.function("radians", Double::class.java, cb.literal(latitude))
        val lonRad = cb.function("radians", Double::class.java, cb.literal(longitude))
        val latColRad = cb.function("radians", Double::class.java, root.get<Double>("latitude"))
        val lonColRad = cb.function("radians", Double::class.java, root.get<Double>("longitude"))

        // Haversine formula
        val distance = cb.prod(
            6371.0,
            cb.function(
                "acos",
                Double::class.java,
                cb.sum(
                    cb.prod(
                        cb.prod(
                            cb.function("cos", Double::class.java, latRad),
                            cb.function("cos", Double::class.java, latColRad)
                        ),
                        cb.function("cos", Double::class.java, cb.diff(lonColRad, lonRad))
                    ),
                    cb.prod(
                        cb.function("sin", Double::class.java, latRad),
                        cb.function("sin", Double::class.java, latColRad)
                    )
                )
            )
        )

        // Create query
        cq.select(root).where(cb.le(distance, radius))

        return entityManager.createQuery(cq).resultList
    }
}
