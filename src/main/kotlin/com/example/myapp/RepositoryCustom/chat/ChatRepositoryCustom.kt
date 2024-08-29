package com.example.myapp.RepositoryCustom.chat

import com.example.myapp.Entity.Chat

interface ChatRepositoryCustom {

    fun findLastChatByRoomId(roomId:String):Chat?
}