package com.example.myapp.Entity

import jakarta.persistence.Id

class ChatRoom {

    @Id
    var id: String? = null

    var chatters: List<String>? = arrayListOf()
    var type:String? = null
    var content: String? = null
    var commuSeq: Long? = null

    fun ChatRoom() {}

}