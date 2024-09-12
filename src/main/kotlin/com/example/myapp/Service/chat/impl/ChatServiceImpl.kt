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

    override fun findLastChatByRoomId(roomId: String): Chat? {
        return chatRepository.findLastChatByRoomId(roomId)
    }

    override fun findByRoomId(roomId: String): ChatRoom? {
        return chatRoomRepository.findById(roomId).orElse(null)
    }

    override fun addChatRoom(chatRoom: ChatRoom) {
        chatRoomRepository.save(chatRoom)
    }

    override fun updateChatRoom(chatRoom: ChatRoom) {
        chatRoomRepository.save(chatRoom)
    }

    override fun readMessage(userSeq: Long, roomId: String): List<Chat> {
        val chatList = chatRepository.findUnreadMessage(userSeq, roomId)
        chatList.forEach { chat -> chat.unread.remove(userSeq) }
        return chatRepository.saveAll(chatList)
    }

    override fun countUnreadRoomMessage(userSeq: Long, roomId: String): Long {
        return chatRepository.countUnreadMessage(userSeq, roomId)
    }

    override fun findPublicRoom(chatters: List<Long>, commuSeq: Long): ChatRoom? {
        return chatRoomRepository.findByCommunityAndUsers(chatters, commuSeq)
    }

    override fun findByUserSeq(userSeq: Long, roomType:String): List<ChatRoom>? {
        return chatRoomRepository.findByUserSeqAndRoomType(userSeq, roomType)
    }
}