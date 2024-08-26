package com.example.myapp.Controller;

import com.example.myapp.Dto.ChatRoomDto
import com.example.myapp.Entity.ChatRoom
import com.example.myapp.Service.chat.ChatService;
import com.example.myapp.Util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID

@RestController
@RequestMapping("/chat/")
class ChatController @Autowired constructor(
    private var chatService:ChatService,
    private var jwtUtil: JwtUtil
) {
    private val log = LoggerFactory.getLogger(JwtUtil::class.java)
    private val tempChatRoom = mutableMapOf<String, ChatRoom>()

    @RequestMapping("addChatRoom")
    fun addChatRoom(@RequestBody chatRoomDto: ChatRoomDto, request: HttpServletRequest):String{
        val userSeq = jwtUtil.JwtTokenGetUserSeq(mapOf("AccessToken" to request.getHeader("AccessToken")))
        val chatters : List<Long> = listOf(userSeq).plus(chatRoomDto.chatters)
        val chatRoom : ChatRoom = ChatRoom.Builder().setChatters(chatters)
                                                    .setCommuSeq(chatRoomDto.commuSeq)
                                                    .setType(chatRoomDto.type)
                                                    .build()
        val dbRoom = chatService.findPublicRoom(chatters, chatRoomDto.commuSeq)
        if(dbRoom!=null){
            return dbRoom.id!!
        }
        val key = UUID.randomUUID().toString()
        chatRoom.id = key
        tempChatRoom[key] = chatRoom
        return key
    }

    @RequestMapping("saveTempRoom")
    fun saveTempRoom(@RequestBody key:String){
        chatService.addChatRoom(tempChatRoom[key]!!)
        tempChatRoom.remove(key)
        log.info("temp Room saved")
    }

    @RequestMapping("deleteTempRoom")
    fun deleteTempRoom(@RequestBody key:String){
        tempChatRoom.remove(key)
        log.info("temp Room deleted")
    }

    @RequestMapping("test")
    fun test():String{
        return "Ok"
    }
}
