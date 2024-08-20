package com.example.myapp.Service.chat

import com.example.myapp.Entity.Chat

interface ChatService {
    fun chatting(chat:Chat)
    fun entrance(roomId:String) : List<Chat>?
}