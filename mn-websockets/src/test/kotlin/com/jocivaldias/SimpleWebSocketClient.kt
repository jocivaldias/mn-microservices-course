package com.jocivaldias

import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.ClientWebSocket
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.reactivex.Single
import java.util.concurrent.ConcurrentLinkedQueue

@ClientWebSocket("/ws/simple/prices")
abstract class SimpleWebSocketClient: AutoCloseable{

    val observedMessages: ConcurrentLinkedQueue<String> = ConcurrentLinkedQueue()
    lateinit var session: WebSocketSession

    @OnOpen
    fun onOpen(session: WebSocketSession){
        this.session = session
    }

    @OnMessage
    fun onMessage(message: String){
        observedMessages.add(message)
    }

    abstract fun send(message: String)

    abstract fun sendReactive(message: String): Single<String>
}