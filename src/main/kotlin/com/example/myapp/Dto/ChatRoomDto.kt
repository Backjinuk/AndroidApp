package com.example.myapp.Dto

class ChatRoomDto {
    var id: String? = null
    var userSeq: Long? = null
    var chatters: List<Long> = listOf()
    var type: String = ""
    var content: String? = null
    var commuSeq: Long = 0
}