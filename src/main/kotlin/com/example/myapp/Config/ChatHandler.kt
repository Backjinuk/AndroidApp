package com.example.myapp.Config

import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

class ChatHandler : TextWebSocketHandler() {
    private val logger = LoggerFactory.getLogger(ChatHandler::class.java)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        logger.info("Connected to session: ${session.id}")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        logger.info("Received message: ${message.payload}")
        session.sendMessage(TextMessage("Echo: ${message.payload}"))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        logger.info("Session closed: ${session.id}")
    }
}