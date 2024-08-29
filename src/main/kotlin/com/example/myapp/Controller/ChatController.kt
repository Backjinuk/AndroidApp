package com.example.myapp.Controller;

import com.example.myapp.Dto.ChatRoomDto
import com.example.myapp.Entity.ChatRoom
import com.example.myapp.Service.chat.ChatService;
import com.example.myapp.Util.JwtUtil
import jakarta.servlet.http.HttpServletRequest
import org.modelmapper.ModelMapper
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
    private var jwtUtil: JwtUtil,
) {
    private val log = LoggerFactory.getLogger(JwtUtil::class.java)
    private val tempChatRoom = mutableMapOf<String, ChatRoom>()
    private var modelMapper:ModelMapper = ModelMapper()

    @RequestMapping("addChatRoom")
    fun addChatRoom(@RequestBody chatRoomDto: ChatRoomDto, request: HttpServletRequest):String{
        val userSeq = jwtUtil.JwtTokenGetUserSeq(mapOf("AccessToken" to request.getHeader("AccessToken")))
        val chatters : List<Long> = listOf(userSeq).plus(chatRoomDto.chatters)
        val chatRoom : ChatRoom = ChatRoom.Builder().setChatters(chatters)
                                                    .setCommuSeq(chatRoomDto.commuSeq)
                                                    .setType(chatRoomDto.type)
                                                    .build()
        println("chatRoom = ${chatRoom}")
        val dbRoom = chatService.findPublicRoom(chatters, chatRoomDto.commuSeq)
        println("dbRoom = ${dbRoom}")
        if(dbRoom!=null){
            return dbRoom.id!!
        }
        val key = UUID.randomUUID().toString()
        chatRoom.id = key
        tempChatRoom[key] = chatRoom
        return key
    }

    @RequestMapping("saveTempRoom")
    fun saveTempRoom(@RequestBody key:String) : ChatRoom{
        val chatRoom = tempChatRoom[key]!!
        chatService.addChatRoom(chatRoom)
        tempChatRoom.remove(key)
        log.info("temp Room saved")
        return chatRoom
    }

    @RequestMapping("deleteTempRoom")
    fun deleteTempRoom(@RequestBody key:String){
        tempChatRoom.remove(key)
        log.info("temp Room deleted")
    }

    fun findMyChatRooms(userSeq:Long, roomType:String):List<ChatRoomDto>{
        val roomList = chatService.findByUserSeq(userSeq, roomType)!!
        val roomDtoList = mutableListOf<ChatRoomDto>()
        for(room in roomList){
            val chatRoomDto = chatRoomToDto(room)
            roomDtoList.add(chatRoomDto)
        }
        return roomDtoList
    }

    @RequestMapping("test")
    fun test():String{
        return "Ok"
    }

    fun chatRoomToDto(chatRoom: ChatRoom):ChatRoomDto{
        return modelMapper.map(chatRoom, ChatRoomDto::class.java)
    }
}
