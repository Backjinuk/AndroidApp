package com.example.myapp.Repository.chat

import com.example.myapp.Entity.Chat
import com.example.myapp.RepositoryCustom.chat.ChatRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRepository : MongoRepository<Chat?, String?>, ChatRepositoryCustom{
    fun findByChatter(chatter:String?): List<Chat>?
    fun findByRoomId(roomId:String?): List<Chat>?
}

