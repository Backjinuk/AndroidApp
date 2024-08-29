package com.example.myapp.Entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document
class ChatRoom private constructor(
    @Field
    val chatters: List<Long>,
    @Field
    val type: String,
    @Field
    val commuSeq: Long,

) {

    @Id
    var id: String? = null
    var content : String? = null
    var chatTime : LocalDateTime? = null

    class Builder {
        private var type: String = ""
        private var chatters: List<Long> = listOf()
        private var commuSeq: Long = 0

        fun setType(type: String) = apply { this.type = type }
        fun setCommuSeq(commuSeq: Long) = apply { this.commuSeq = commuSeq }
        fun setChatters(chatters: List<Long>) = apply { this.chatters = chatters }

        fun build(): ChatRoom {
            return ChatRoom(chatters=chatters, type = type, commuSeq=commuSeq)
        }
    }

    override fun toString(): String {
        return "ChatRoom(chatters=$chatters, type='$type', commuSeq=$commuSeq, id=$id)"
    }

}