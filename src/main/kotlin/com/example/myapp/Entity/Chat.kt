package com.example.myapp.Entity

import jakarta.persistence.Id
import java.time.LocalDateTime

class Chat private constructor(
    val chatter: String,
    val content: String,
    val unread: List<String>,
    val chatTime: LocalDateTime,
    val roomId: String

) {
    @Id
    private var id: String? = null
    class Builder {
        private var chatter: String = ""
        private var content: String = ""
        private var unread: List<String> = arrayListOf()
        private var chatTime: LocalDateTime = LocalDateTime.now()
        private var roomId: String = ""

        fun setChatter(chatter: String) = apply { this.chatter = chatter }
        fun setContent(content: String) = apply { this.content = content }
        fun setUnread(unread: List<String>) = apply { this.unread = unread }
        fun setChatTime(time: LocalDateTime) = apply { this.chatTime = chatTime }
        fun setRoomId(roomId: String) = apply { this.roomId = roomId }

        fun build(): Chat {
            return Chat(chatter= chatter, content=content, unread=unread, chatTime=chatTime, roomId = roomId)
        }
    }

    fun getId() : String?{
        return id
    }

    override fun toString(): String {
        return "Chat(id=$id, chatter='$chatter', content='$content', unread=$unread, chatTime=$chatTime, roomId='$roomId')"
    }
}