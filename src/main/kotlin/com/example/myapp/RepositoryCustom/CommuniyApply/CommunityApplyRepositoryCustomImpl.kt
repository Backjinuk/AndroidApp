package com.example.myapp.RepositoryCustom.CommuniyApply

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Entity.CommunityApply
import com.example.myapp.Util.ModelMapperUtil.Companion.communityApplyEntityToDto
import com.linecorp.kotlinjdsl.QueryFactoryImpl
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.selectQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class CommunityApplyRepositoryCustomImpl @Autowired constructor(
    @PersistenceContext private val entityManager : EntityManager,
    private val queryFactory : QueryFactoryImpl
): CommunityApplyRepositoryCustom{


    override fun getCommunityApplyList(communityApply: CommunityApply): List<CommunityApplyDto> {
        return queryFactory.selectQuery<CommunityApply> {
            select(entity(CommunityApply::class))
            from(entity(CommunityApply::class))
            where( col(CommunityApply::applyUserSeq).equal(communityApply.applyUserSeq) )
        }.resultList
            .map { communityApply -> communityApplyEntityToDto(communityApply) }
            .toList()
    }


}