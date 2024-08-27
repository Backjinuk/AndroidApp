package com.example.myapp.RepositoryCustom.CommunityApply

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Entity.CommunityApply
import com.example.myapp.Entity.QCommunityApply
import com.example.myapp.RepositoryCustom.CommuniyApply.CommunityApplyRepositoryCustom
import com.example.myapp.Util.ModelMapperUtil.Companion.communityApplyEntityToDto
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CommunityApplyRepositoryCustomImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : CommunityApplyRepositoryCustom {

    @Transactional(readOnly = true)
    override fun getCommunityApplyList(communityApply: CommunityApply): List<CommunityApplyDto> {
        // Q 클래스 인스턴스 생성
        val qCommunityApply = QCommunityApply.communityApply

        // QueryDSL 쿼리 작성
        val results = queryFactory
            .selectFrom(qCommunityApply)
            .where(qCommunityApply.applyUserSeq.eq(communityApply.applyUserSeq))
            .fetch()

        // 결과를 DTO로 변환하여 반환
        return results.map { communityApplyEntityToDto(it) }
    }
}
