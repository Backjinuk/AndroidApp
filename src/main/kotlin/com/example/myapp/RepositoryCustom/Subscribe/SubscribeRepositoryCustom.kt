package com.example.myapp.RepositoryCustom.Subscribe

import com.example.myapp.Dto.SubscribeDto
import com.example.myapp.Entity.Subscribe

interface SubscribeRepositoryCustom {

    fun addSubscribe(subscribeDtoToEntity: Subscribe): Boolean

    fun getSubscribe(subscribeDtoToEntity: Subscribe): Subscribe?

    fun getSubscribeList(subscribe: Subscribe): List<SubscribeDto>
}