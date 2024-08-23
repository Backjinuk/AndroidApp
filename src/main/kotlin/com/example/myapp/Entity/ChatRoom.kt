package com.example.myapp.Entity

import jakarta.persistence.Id
import java.time.LocalDateTime

class ChatRoom private constructor(
    val chatters: List<Long>,
    val type: String,
    val commuSeq: Long,

) {

    @Id
    var id: String? = null

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