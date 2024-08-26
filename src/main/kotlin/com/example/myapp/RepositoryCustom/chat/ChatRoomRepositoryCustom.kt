package com.example.myapp.RepositoryCustom.chat

import com.example.myapp.Entity.ChatRoom
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepositoryCustom {

    fun findByCommunityAndUsers(chatters : List<Long>, commuSeq:Long) : ChatRoom?

    fun findByUserSeq(userSeq:Long) : List<ChatRoom>?

}