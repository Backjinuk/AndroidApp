package com.example.myapp.Repository.chat

import com.example.myapp.Entity.Chat
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRepository : MongoRepository<Chat?, String?>{
    fun findByChatter(chatter:String?): List<Chat>?
    fun findByRoomId(roomId:String?): List<Chat>?
}

