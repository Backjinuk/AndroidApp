package com.example.myapp.Config

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

@Component
class ChatHandler(
    private val chatService: ChatService
) : TextWebSocketHandler() {

    private val logger = LoggerFactory.getLogger(ChatHandler::class.java)
    private val sessionInRoom = mutableMapOf<String, String>()
    private val rooms = mutableMapOf<String, ArrayList<WebSocketSession>>()
    private val mapper = ObjectMapper()

    init {
        mapper.registerModule(JavaTimeModule())
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connected to session: ${session.id}")
        val roomId = session.uri.toString().split("=")[1]
        val returnMap = mutableMapOf<String, Any?>()
        returnMap["type"] = "list"
        returnMap["payload"] = chatService.entrance(roomId)

        session.sendMessage(TextMessage(mapper.writeValueAsString(returnMap)))
        sessionInRoom[session.id] = roomId
        val room = rooms.getOrDefault(roomId, arrayListOf())
        room.add(session)
        rooms[roomId] = room
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received message: ${message.payload}")
        val map = mapper.readValue<Map<String, String>>(content = message.payload)
        val chatter = map["chatter"]
        val content = map["content"]
        val roomId = map["roomId"]
        val chat = Chat.Builder().setChatter(chatter!!).setContent(content!!).setRoomId(roomId!!).build()
        logger.info("Received message: $chat")
        chatService.chatting(chat)
        val returnMap = mutableMapOf<String, Any>()
        returnMap["type"] = "message"
        returnMap["payload"] = chat
        logger.info("Received message: $chat")
        val room = rooms[roomId]!!
        for(roomSession in room){
            if(roomSession == session)continue
            if(roomSession.isOpen) roomSession.sendMessage(TextMessage(mapper.writeValueAsString(returnMap)))
            else room.remove(roomSession)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val roomId = sessionInRoom[session.id]!!
        val room = rooms[roomId]!!
        room.remove(session)
        println(room.size)
        if(room.isEmpty()) rooms.remove(roomId)
        sessionInRoom.remove(session.id)

        logger.info("Session closed: ${session.id}")
    }

}
