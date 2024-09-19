package com.example.myapp.RepositoryCustom.chat

import com.example.myapp.Entity.Chat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update

class ChatRepositoryCustomImpl @Autowired constructor(
    private val template: MongoTemplate
):ChatRepositoryCustom {
    override fun findLastChatByRoomId(roomId: String): Chat? {
        val query = Query(Criteria.where("roomId").`is`(roomId)).with(Sort.by(Sort.Direction.DESC, "chatTime"))
        val re = template.query(Chat::class.java)
            .matching(query).firstValue()
        return re
    }

    override fun findUnreadMessage(userSeq: Long, roomId: String): List<Chat> {
        val query = Query(Criteria.where("roomId").`is`(roomId).and("unread").`is`(userSeq))
        val re = template.query(Chat::class.java).matching(query).all()
        return re
    }

    override fun countUnreadMessage(userSeq: Long, roomId: String): Long {
        val query = Query(Criteria.where("roomId").`is`(roomId).and("unread").`is`(userSeq))
        val re = template.query(Chat::class.java).matching(query).count()
        return re
    }

    override fun countUnreadMessage(userSeq: Long): Long {
        val query = Query(Criteria.where("unread").`is`(userSeq))
        val re = template.query(Chat::class.java).matching(query).count()
        return re
    }
}