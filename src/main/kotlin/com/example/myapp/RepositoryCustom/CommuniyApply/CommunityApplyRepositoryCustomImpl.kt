package com.example.myapp.RepositoryCustom.CommunityApply

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Entity.CommunityApply
import com.example.myapp.Entity.QCommunity
import com.example.myapp.Entity.QCommunityApply
import com.example.myapp.RepositoryCustom.CommuniyApply.CommunityApplyRepositoryCustom
import com.example.myapp.Util.ModelMapperUtil.Companion.communityApplyEntityToDto
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class CommunityApplyRepositoryCustomImpl(
    private val entityManager: EntityManager,
    private val queryFactory: JPAQueryFactory
) : CommunityApplyRepositoryCustom {

    // Q 클래스 인스턴스 생성
    val qCommunityApply = QCommunityApply.communityApply

    val qCommunity = QCommunity.community

    @Transactional(readOnly = true)
    override fun getCommunityApplyList(communityApply: CommunityApply): List<CommunityApplyDto> {

        // QueryDSL 쿼리 작성
        val results = queryFactory
            .selectFrom(qCommunityApply)
            .where(qCommunityApply.applyUserSeq.eq(communityApply.applyUserSeq))
            .fetch()

        // 결과를 DTO로 변환하여 반환
        return results.map { communityApplyEntityToDto(it) }
    }

    override fun getCommunityApplyUser(communityApply: CommunityApply): CommunityApplyDto {

        val result = queryFactory
            .selectFrom(qCommunityApply)
            .where(
                qCommunityApply.applyUserSeq.eq(communityApply.applyUserSeq)
                .and(qCommunityApply.applyCommuSeq.eq(communityApply.applyCommuSeq))
            )
            .fetchOne()

        return if (result != null){
            communityApplyEntityToDto(result)
        }else{
            println("communityApplyDto 값이 없음")
            CommunityApplyDto()
        }
    }

    @Transactional
    override fun addCommunityApply(commuApply: CommunityApply): Boolean {



        // 기존 레코드 확인
        val result = queryFactory
            .selectFrom(qCommunityApply)
            .where(
                qCommunityApply.applyUserSeq.eq(commuApply.applyUserSeq)
                    .and(qCommunityApply.applyCommuSeq.eq(commuApply.applyCommuSeq))
            ).fetchOne()

        // 변수 초기화 및 설정
        val (userCount, applyStatus, applyCheck) = if (result != null) {
            val updatedStatus = if (result.applyStatus == 'Y') 'N' else 'Y'
            val countChange = if (result.applyStatus == 'Y') -1 else 1
            val applyCheck = result.applyStatus != 'Y'
            Triple(countChange, updatedStatus, applyCheck)
        } else {
            Triple(1, 'Y', true)
        }

        if (result != null) {

            // 레코드가 존재하면 업데이트
            queryFactory
                .update(qCommunityApply)
                .set(qCommunityApply.applyStatus, applyStatus) // 문자열 리터럴 대신 Char 사용
                .set(qCommunityApply.upDt, LocalDateTime.now())
                .where(
                    qCommunityApply.applyUserSeq.eq(commuApply.applyUserSeq)
                        .and(qCommunityApply.applyCommuSeq.eq(commuApply.applyCommuSeq))
                )
                .execute()

        } else {
            // 레코드가 존재하지 않으면 삽입
            queryFactory
                .insert(qCommunityApply)
                .columns(
                    qCommunityApply.applyUserSeq,
                    qCommunityApply.applyCommuSeq,
                    qCommunityApply.upDt,
                    qCommunityApply.regDt,
                    qCommunityApply.applyStatus
                )
                .values(
                    commuApply.applyUserSeq,
                    commuApply.applyCommuSeq,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    'Y' // Char 사용
                )
                .execute()
        }

        println("userCount = ${userCount}")

        queryFactory
            .update(qCommunity)
            .set(qCommunity.userCount, qCommunity.userCount.add(userCount))
            .where(qCommunity.commuSeq.eq(commuApply.applyCommuSeq))
            .execute()

        return applyCheck
    }

}
