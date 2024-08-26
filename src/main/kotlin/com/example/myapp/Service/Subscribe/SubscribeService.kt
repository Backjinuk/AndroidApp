package com.example.myapp.Service.Subscribe

import com.example.myapp.Dto.SubscribeDto
import com.example.myapp.Entity.Subscribe

interface SubscribeService {

    fun addSubscribe(subscribeDtoToEntity: Subscribe): Boolean

    fun getSubscribe(subscribeDtoToEntity: Subscribe): Subscribe?
    
    fun getSubscribeList(subscribeDtoToEntity: Subscribe): List<SubscribeDto>

}