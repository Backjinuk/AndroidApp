package com.example.myapp.RepositoryCustom.chat

import com.example.myapp.Entity.Chat
import com.example.myapp.Entity.ChatRoom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class ChatRepositoryCustomImpl @Autowired constructor(
    private val template: MongoTemplate
):ChatRepositoryCustom {
    override fun findLastChatByRoomId(roomId: String): Chat? {
        val query = Query(Criteria.where("roomId").`is`(roomId)).with(Sort.by(Sort.Direction.DESC, "chatTime"))
        val re = template.query(Chat::class.java)
            .matching(query).firstValue()
        return re
    }
}