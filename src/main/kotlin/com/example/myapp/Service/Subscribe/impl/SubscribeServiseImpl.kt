package com.example.myapp.Service.Subscribe.impl

import com.example.myapp.Dto.SubscribeDto
import com.example.myapp.Entity.Subscribe
import com.example.myapp.Repository.Subscribe.SubscribeRepository
import com.example.myapp.Service.Subscribe.SubscribeService
import org.springframework.stereotype.Service

@Service
class SubscribeServiseImpl(
    private val subscribeRepository: SubscribeRepository
) : SubscribeService {

    override fun addSubscribe(subscribeDtoToEntity: Subscribe): Boolean {
        return subscribeRepository.addSubscribe(subscribeDtoToEntity);
    }

    override fun getSubscribe(subscribeDtoToEntity: Subscribe): Subscribe? {
        return subscribeRepository.getSubscribe(subscribeDtoToEntity);
    }

    override fun getSubscribeList(subscribeDtoToEntity: Subscribe): List<SubscribeDto> {
        return subscribeRepository.getSubscribeList(subscribeDtoToEntity);
    }

}