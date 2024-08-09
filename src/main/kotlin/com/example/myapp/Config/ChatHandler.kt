package com.example.myapp.Config

import com.example.myapp.Service.ChatService
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
    private val sessions = mutableMapOf<String, WebSocketSession>()
    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connected to session: ${session.id}")
        sessions[session.id] = session
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received message: ${message.payload}")
        logger.info("Received message: ${chatService.asdf()}")
        for(key in sessions.keys){
            if(key==session.id){
                continue
            }
            sessions[key]?.sendMessage(TextMessage("Echo: ${message.payload}"))
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
        logger.info("Session closed: ${session.id}")
    }
}
