package com.example.myapp.RepositoryCustom.Subscribe

import com.example.myapp.Entity.Subscribe

interface SubscribeRepositoryCustom {
    fun addSubscribe(subscribeDtoToEntity: Subscribe): Boolean
}