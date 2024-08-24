package com.example.myapp.Entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document
class Chat private constructor(
    @Field
    val chatter: Long,
    @Field
    val chatterId: String,
    @Field
    val content: String,
    @Field
    val unread: List<String>,
    @Field
    val chatTime: LocalDateTime,
    @Field
    val roomId: String

) {
    @Id
    private var id: String? = null
    class Builder {
        private var chatter: Long = 0
        private var chatterId: String = ""
        private var content: String = ""
        private var unread: List<String> = arrayListOf()
        private var chatTime: LocalDateTime = LocalDateTime.now()
        private var roomId: String = ""

        fun setChatter(chatter: Long) = apply { this.chatter = chatter }
        fun setChatterId(chatterId: String) = apply { this.chatterId = chatterId }
        fun setContent(content: String) = apply { this.content = content }
        fun setUnread(unread: List<String>) = apply { this.unread = unread }
        fun setChatTime(time: LocalDateTime) = apply { this.chatTime = chatTime }
        fun setRoomId(roomId: String) = apply { this.roomId = roomId }

        fun build(): Chat {
            return Chat(chatter= chatter, chatterId=chatterId, content=content, unread=unread, chatTime=chatTime, roomId = roomId)
        }
    }

    fun getId() : String?{
        return id
    }

    override fun toString(): String {
        return "Chat(chatter=$chatter, chatterId='$chatterId', content='$content', unread=$unread, chatTime=$chatTime, roomId='$roomId', id=$id)"
    }

}