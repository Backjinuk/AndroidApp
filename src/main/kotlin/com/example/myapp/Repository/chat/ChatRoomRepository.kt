package com.example.myapp.Repository.chat

import com.example.myapp.Entity.ChatRoom
import org.springframework.data.mongodb.repository.MongoRepository

interface ChatRoomRepository : MongoRepository<ChatRoom?, String?> {
}