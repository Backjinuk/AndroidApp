package com.example.myapp.RepositoryCustom.chat

import com.example.myapp.Entity.ChatRoom
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

class ChatRoomRepositoryCustomImpl @Autowired constructor(
    private val template:MongoTemplate
):ChatRoomRepositoryCustom
{
    override fun findByCommunityAndUsers(chatters: List<Long>, commuSeq: Long): ChatRoom? {
        val query = Query(Criteria.where("commuSeq").`is`(commuSeq).and("chatters").`is`(chatters))
        val re = template.query(ChatRoom::class.java)
            .matching(query).oneValue()
        return re
    }

    override fun findByUserSeqAndRoomType(userSeq: Long, roomType: String): List<ChatRoom>? {
        val query = Query(Criteria.where("chatters").`is`(userSeq).and("type").`is`(roomType))
        val re = template.query(ChatRoom::class.java)
            .matching(query).all()
        return re
    }

}