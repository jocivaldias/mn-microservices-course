package com.jocivaldias

import io.micronaut.websocket.CloseReason
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@ServerWebSocket("/ws/simple/prices")
class SimpleWebSocketServer {

    private val log: Logger = LoggerFactory.getLogger(SimpleWebSocketServer::class.java)

    @OnOpen
    fun onOpen(session: WebSocketSession): Publisher<String>{
        return session.send("Connected!")
    }

    @OnMessage
    fun onMessage(message: String, session: WebSocketSession): Publisher<String>{
        log.info("Receveid message: {} from session {}", message, session.id)
        if (message.contentEquals("disconnect me")){
            log.info("Client close requested!")
            session.close(CloseReason.NORMAL)
            return Flowable.empty()
        }

        return session.send("Not supported => ($message)")
    }

    @OnClose
    fun onClose(session: WebSocketSession){
        log.info("Session closed: {}", session.id)
    }
}