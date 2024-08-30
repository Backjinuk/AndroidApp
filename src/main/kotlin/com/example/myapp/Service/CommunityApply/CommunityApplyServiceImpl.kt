package com.example.myapp.Service.CommunityApply

import com.example.myapp.Dto.CommunityApplyDto
import com.example.myapp.Entity.CommunityApply
import com.example.myapp.RepositoryCustom.CommuniyApply.CommunityApplyRepositoryCustom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommunityApplyServiceImpl @Autowired constructor(
    private val communityApplyRepository: CommunityApplyRepositoryCustom
) : CommunityApplyService {
    override fun getCommunityApplyList(communityApply: CommunityApply): List<CommunityApplyDto> {
        return communityApplyRepository.getCommunityApplyList(communityApply);
    }

    override fun getCommuApplyUser(communityApplyDtoToEntity: CommunityApply): CommunityApplyDto {
        return communityApplyRepository.getCommunityApplyUser(communityApplyDtoToEntity)
    }

    override fun addCommunityApply(commuApply: CommunityApply): Boolean {
        return communityApplyRepository.addCommunityApply(commuApply);
    }
}