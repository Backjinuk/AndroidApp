package com.example.myapp.Service.chat

import com.example.myapp.Entity.Chat
import com.example.myapp.Entity.ChatRoom

interface ChatService {
    fun chatting(chat:Chat)
    fun entrance(roomId:String) : List<Chat>?
    fun findByRoomId(roomId:String) : ChatRoom?
    fun findLastChatByRoomId(roomId: String): Chat?
    fun addChatRoom(chatRoom: ChatRoom)
    fun updateChatRoom(chatRoom: ChatRoom)
    fun readMessage(userSeq: Long, roomId: String):List<Chat>
    fun countUnreadRoomMessage(userSeq: Long, roomId: String):Long
    fun countUnreadMessage(userSeq: Long):Long

    fun findPublicRoom(chatters:List<Long>, commuSeq:Long): ChatRoom?

    fun findByUserSeq(userSeq:Long, roomType:String):List<ChatRoom>?
}