package com.example.myapp.Service.chat.impl

import com.example.myapp.Entity.Chat
import com.example.myapp.Repository.chat.ChatRepository
import com.example.myapp.Service.chat.ChatService
import org.springframework.stereotype.Service

@Service
class ChatServiceImpl(
    private var chatRepository: ChatRepository
) : ChatService {


    override fun chatting(chat: Chat) {
        chatRepository.save(chat)
    }

    override fun entrance(roomId: String): List<Chat>? {
        return chatRepository.findByRoomId(roomId)
    }
}