package com.example.myapp.Config

import com.example.myapp.Controller.ChatController
import com.example.myapp.Entity.Chat
import com.example.myapp.Service.chat.ChatService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.net.URI

@Component
class ChatHandler(
    private val chatController: ChatController,
    private val chatService: ChatService
) : TextWebSocketHandler() {

    private val logger = LoggerFactory.getLogger(ChatHandler::class.java)
    private val sessionsForCount = mutableMapOf<Long, WebSocketSession>()//userSeq, session
    private val sessionsForRoomList = mutableMapOf<Long, WebSocketSession>()//userSeq, session
    private val sessionsForChat = mutableMapOf<Long, WebSocketSession>()//userSeq, session
    private val rooms = mutableMapOf<String, ArrayList<WebSocketSession>>()//roomId, sessions
    private val mapper = ObjectMapper()

    init {
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connected to session: ${session.id}")
        logger.info("Connected to session: ${session.uri}")
        val inputMap = parseUri(session.uri)
        when(inputMap["type"]){
            "chatRoom"->{
                logger.info("Connected to session: chatRoom")
                val roomId = inputMap["roomId"]!!
                val returnMap = mutableMapOf<String, Any?>()
                returnMap["type"] = "message"
                returnMap["payload"] = chatService.entrance(roomId)

                session.sendMessage(messageFrom(returnMap))
                val room = rooms.getOrDefault(roomId, arrayListOf())
                room.add(session)
                rooms[roomId] = room
            }
            "chatRoomList"->{
                logger.info("Connected to session: chatRoomList")
                val userSeq = inputMap["userSeq"]!!.toLong()
                val returnMap = mutableMapOf<String, Any?>()
                returnMap["type"] = "rooms"
                returnMap["payload"] = chatService.findMyRooms(userSeq)
                session.sendMessage(messageFrom(returnMap))
                sessionsForRoomList[userSeq] = session
            }
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received message: ${message.payload}")
        val map = mapper.readValue<Map<String, String>>(content = message.payload)
        val chatter = map["chatter"]?.toLong()
        val chatterId = map["chatterId"]
        val content = map["content"]
        val roomId = map["roomId"]!!
        if(chatService.findByRoomId(roomId)==null){
            chatController.saveTempRoom(roomId)
        }
        val chat = Chat.Builder().setChatter(chatter!!).setChatterId(chatterId!!).setContent(content!!).setRoomId(roomId!!).build()
        logger.info("Received message: $chat")
        chatService.chatting(chat)
        val returnMap = mutableMapOf<String, Any>()
        returnMap["type"] = "message"
        returnMap["payload"] = listOf(chat)
        logger.info("Received message: $chat")
        val room = rooms[roomId]!!
        for(roomSession in room){
            if(roomSession == session)continue
            if(roomSession.isOpen) roomSession.sendMessage(messageFrom(returnMap))
            else room.remove(roomSession)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val inputMap = parseUri(session.uri)
        when(inputMap["type"]){
            "chatRoom"->{
                val roomId = inputMap["roomId"]!!
                val room = rooms[roomId]!!
                room.remove(session)
                println(room.size)
                if(room.isEmpty()) rooms.remove(roomId)
                if(chatService.findByRoomId(roomId)==null){
                    chatController.deleteTempRoom(roomId)
                }
            }
            "chatRoomList"->{
                val userSeq = inputMap["userSeq"]!!.toLong()
                sessionsForRoomList.remove(userSeq)
            }
        }

        logger.info("Session closed: ${session.id}")
    }

    fun messageFrom(map:Map<String, Any?>):TextMessage{
        return TextMessage(mapper.writeValueAsString(map))
    }

    fun parseUri(uri:URI?):Map<String, String>{
        val inputMap = mutableMapOf<String, String>()
        for(entries in uri.toString().split("?")[1].split("&")){
            val key = entries.split("=")[0]
            val value = entries.split("=")[1]
            inputMap[key] = value
        }
        return inputMap
    }

}
