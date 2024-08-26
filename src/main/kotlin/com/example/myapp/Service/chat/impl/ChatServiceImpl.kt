package com.example.myapp.Service.chat.impl

import com.example.myapp.Entity.Chat
import com.example.myapp.Entity.ChatRoom
import com.example.myapp.Repository.chat.ChatRepository
import com.example.myapp.Repository.chat.ChatRoomRepository
import com.example.myapp.Service.chat.ChatService
import org.springframework.stereotype.Service

@Service
class ChatServiceImpl(
    private var chatRepository: ChatRepository,
    private var chatRoomRepository: ChatRoomRepository
) : ChatService {


    override fun chatting(chat: Chat) {
        chatRepository.save(chat)
    }

    override fun entrance(roomId: String): List<Chat>? {
        return chatRepository.findByRoomId(roomId)
    }

    override fun findByRoomId(roomId: String): ChatRoom? {
        return chatRoomRepository.findById(roomId).orElse(null)
    }

    override fun addChatRoom(chatRoom: ChatRoom) {
        chatRoomRepository.save(chatRoom)
    }

    override fun findPublicRoom(chatters: List<Long>, commuSeq: Long): ChatRoom? {
        return chatRoomRepository.findByCommunityAndUsers(chatters, commuSeq)
    }

    override fun findMyRooms(userSeq: Long): List<ChatRoom>? {
        return chatRoomRepository.findByUserSeq(userSeq)
    }
}