package com.example.myapp.Controller;

import com.example.myapp.Dto.ChatRoomDto
import com.example.myapp.Entity.Chat
import com.example.myapp.Entity.ChatRoom
import com.example.myapp.Service.chat.ChatService;
import com.example.myapp.Util.JwtUtil
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.modelmapper.ModelMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.time.LocalDateTime
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
    var sessionsForPublicRoomList:MutableMap<Long, WebSocketSession>? = null
    var mapper: ObjectMapper? = null

    @RequestMapping("addChatRoom")
    fun addChatRoom(@RequestBody chatRoomDto: ChatRoomDto, request: HttpServletRequest):String{
        val userSeq = getUserSeqFromRequest(request)
        val chatters = mutableListOf(userSeq)
        chatters.add(chatRoomDto.chatters[0])
        var chatRoomBuilder = ChatRoom.Builder().setType(chatRoomDto.type).setChatters(chatters)
        var chatRoom : ChatRoom? = null
        var dbRoom : ChatRoom? = null
        when(chatRoomDto.type){
            "public"->{
                chatRoom = chatRoomBuilder.setCommuSeq(chatRoomDto.commuSeq).build()
                dbRoom = chatService.findPublicRoom(chatters, chatRoomDto.commuSeq)
            }
            "private"->{
                chatRoom = chatRoomBuilder.build()
                dbRoom = chatService.findPrivateRoom(chatters)
            }
        }
        if(dbRoom!=null){
            return dbRoom.id!!
        }
        val key = UUID.randomUUID().toString()
        chatRoom!!.id = key
        tempChatRoom[key] = chatRoom
        return key
    }

    @RequestMapping("addGroupRoom")
    fun addGroupRoom(@RequestBody chatRoomDto: ChatRoomDto, request: HttpServletRequest){
        val userSeq = getUserSeqFromRequest(request)
        val chatRoom = ChatRoom.Builder().setType("group")
                                            .setChatters(mutableListOf(userSeq))
                                            .setCommuSeq(chatRoomDto.commuSeq)
                                            .build()
        chatRoom.content = "새로운 모임이 시작되었습니다."
        chatRoom.chatTime = LocalDateTime.now()
        chatService.addChatRoom(chatRoom)
        sendSessionMessageForGroup(chatRoom)
    }

    @RequestMapping("addMember")
    fun addMember(@RequestBody chatRoomDto: ChatRoomDto, request: HttpServletRequest){
        val userSeq = getUserSeqFromRequest(request)
        var chatRoom = chatService.findGroupRoom(chatRoomDto.commuSeq)!!
        chatRoom.chatters.add(userSeq)
        chatRoom.content = "새로운 회원이 가입하였습니다."
        chatRoom.chatTime = LocalDateTime.now()
        chatService.updateChatRoom(chatRoom)
        sendSessionMessageForGroup(chatRoom)
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

    @RequestMapping("countMyMessages")
    fun countMyMessages(@RequestBody userSeq:Long) : Long{
        return chatService.countUnreadMessage(userSeq)
    }

    fun findMyChatRooms(userSeq:Long, roomType:String):List<ChatRoomDto>{
        val roomList = chatService.findByUserSeq(userSeq, roomType)!!
        val roomDtoList = mutableListOf<ChatRoomDto>()
        for(room in roomList){
            val chatRoomDto = chatRoomToDto(room)
            chatRoomDto.unreadMessages = chatService.countUnreadRoomMessage(userSeq, chatRoomDto.id!!)
            roomDtoList.add(chatRoomDto)
        }
        return roomDtoList
    }

    fun readMessage(userSeq:Long, roomId:String):List<Chat>{
        return chatService.readMessage(userSeq, roomId)
    }

    @RequestMapping("test")
    fun test():String{
        return "Ok"
    }

    fun chatRoomToDto(chatRoom: ChatRoom):ChatRoomDto{
        return modelMapper.map(chatRoom, ChatRoomDto::class.java)
    }

    fun getUserSeqFromRequest(request: HttpServletRequest):Long{
        return jwtUtil.JwtTokenGetUserSeq(mapOf("AccessToken" to request.getHeader("AccessToken")))
    }

    fun sendSessionMessageForGroup(chatRoom:ChatRoom){
        if(sessionsForPublicRoomList==null||mapper==null)return
        log.info("send Message")
        val returnMap = mutableMapOf<String, Any>()
        returnMap["type"] = "message"
        for(userSeq in chatRoom.chatters){
            log.info("to userSeq$userSeq")
            val chatRoomDto = chatRoomToDto(chatRoom)
            chatRoomDto.unreadMessages = chatService.countUnreadRoomMessage(userSeq, chatRoomDto.id!!)
            returnMap["payload"] = listOf(chatRoomDto)
            sessionsForPublicRoomList!![userSeq]?.sendMessage(messageFrom(returnMap))
        }
    }

    fun messageFrom(map:Map<String, Any?>): TextMessage {
        return TextMessage(mapper!!.writeValueAsString(map))
    }
}
