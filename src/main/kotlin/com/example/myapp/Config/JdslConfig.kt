package com.example.myapp.Config

import com.linecorp.kotlinjdsl.QueryFactory
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import com.linecorp.kotlinjdsl.query.creator.CriteriaQueryCreatorImpl
import com.linecorp.kotlinjdsl.query.creator.SubqueryCreatorImpl
import com.linecorp.kotlinjdsl.spring.data.SpringDataQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JdslConfig {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Bean
    fun queryFactory(): QueryFactoryImpl  {
        val criteriaQueryCreator = CriteriaQueryCreatorImpl(entityManager)
        val subqueryCreator = SubqueryCreatorImpl()
        return QueryFactoryImpl(criteriaQueryCreator, subqueryCreator)
    }
}