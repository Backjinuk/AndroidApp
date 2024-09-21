package com.example.myapp.RepositoryCustom.chat

import com.example.myapp.Entity.ChatRoom
import org.springframework.stereotype.Repository

@Repository
interface ChatRoomRepositoryCustom {

    fun findByCommunityAndUsers(chatters : List<Long>, commuSeq:Long) : ChatRoom?
    fun findPrivateRoomByUsers(chatters : List<Long>) : ChatRoom?
    fun findGroupByCommunity(commuSeq:Long) : ChatRoom?

    fun findByUserSeqAndRoomType(userSeq:Long, roomType:String) : List<ChatRoom>?

}