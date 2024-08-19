package com.example.myapp.Service.Subscribe

import com.example.myapp.Entity.Subscribe

interface SubscribeService {

    fun addSubscribe(subscribeDtoToEntity: Subscribe): Boolean

    fun getSubscribe(subscribeDtoToEntity: Subscribe): Subscribe?
}