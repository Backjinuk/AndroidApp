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

@RestController
@RequestMapping("/chat/")
class ChatController @Autowired constructor(
    private var chatService:ChatService,
    private var jwtUtil: JwtUtil
) {
    private val log = LoggerFactory.getLogger(JwtUtil::class.java)

    @RequestMapping("addChatRoom")
    fun addChatRoom(@RequestBody chatRoomDto: ChatRoomDto, request: HttpServletRequest):String{
        val userSeq = jwtUtil.JwtTokenGetUserSeq(mapOf("AccessToken" to request.getHeader("AccessToken")))
        var chatters : List<Long> = listOf(userSeq).plus(chatRoomDto.chatters)
        var chatRoom : ChatRoom = ChatRoom.Builder().setChatters(chatters)
                                                    .setCommuSeq(chatRoomDto.commuSeq)
                                                    .setType(chatRoomDto.type)
                                                    .build()
        chatService.addChatRoom(chatRoom)
        print(chatRoom)
        return chatRoom.id!!
    }
}
