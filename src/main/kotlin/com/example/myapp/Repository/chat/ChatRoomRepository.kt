package com.example.myapp.Repository.chat

import com.example.myapp.Entity.ChatRoom
import com.example.myapp.RepositoryCustom.chat.ChatRoomRepositoryCustom
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRoomRepository : MongoRepository<ChatRoom?, String?>, ChatRoomRepositoryCustom {
}