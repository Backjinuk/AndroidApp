package com.example.myapp.Service.chat

import com.example.myapp.Entity.Chat
import com.example.myapp.Entity.ChatRoom

interface ChatService {
    fun chatting(chat:Chat)
    fun entrance(roomId:String) : List<Chat>?

    fun addChatRoom(chatRoom: ChatRoom)

    fun findPublicRoom(chatters:List<Long>, commuSeq:Long): ChatRoom?
}