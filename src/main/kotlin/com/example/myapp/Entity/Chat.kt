package com.example.myapp.Entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document
class Chat private constructor(
    @Id
    private var id: String?,
    @Field
    val chatter: Long,
    @Field
    val chatterId: String,
    @Field
    val content: String,
    @Field
    val unread: MutableList<Long>,
    @Field
    val chatTime: LocalDateTime,
    @Field
    val roomId: String

) {
    class Builder {
        private var id:String? = null
        private var chatter: Long = 0
        private var chatterId: String = ""
        private var content: String = ""
        private var unread: MutableList<Long> = arrayListOf()
        private var chatTime: LocalDateTime = LocalDateTime.now()
        private var roomId: String = ""

        fun setId(id:String) = apply{this.id = id}
        fun setChatter(chatter: Long) = apply { this.chatter = chatter }
        fun setChatterId(chatterId: String) = apply { this.chatterId = chatterId }
        fun setContent(content: String) = apply { this.content = content }
        fun setUnread(unread: MutableList<Long>) = apply { this.unread = unread }
        fun setChatTime(chatTime: LocalDateTime) = apply { this.chatTime = chatTime }
        fun setRoomId(roomId: String) = apply { this.roomId = roomId }

        fun build(): Chat {
            return Chat(id=id, chatter= chatter, chatterId=chatterId, content=content, unread=unread, chatTime=chatTime, roomId = roomId)
        }
    }

    fun getId() : String?{
        return id
    }

    override fun toString(): String {
        return "Chat(chatter=$chatter, chatterId='$chatterId', content='$content', unread=$unread, chatTime=$chatTime, roomId='$roomId', id=$id)"
    }

}