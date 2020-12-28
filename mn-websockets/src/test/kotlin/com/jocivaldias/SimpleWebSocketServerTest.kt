package com.jocivaldias

import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.websocket.RxWebSocketClient
import io.micronaut.websocket.WebSocketClient
import org.awaitility.Awaitility
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import javax.inject.Inject

@MicronautTest
class SimpleWebSocketServerTest {

    private val log: Logger = LoggerFactory.getLogger(SimpleWebSocketServerTest::class.java)
    private lateinit var webSocketClient: SimpleWebSocketClient

    @Inject
    @field:Client("http://localhost:8180")
    lateinit var client: RxWebSocketClient

    @BeforeEach
    fun connect(){
        webSocketClient = client.connect(SimpleWebSocketClient::class.java, "/ws/simple/prices").blockingFirst()
        log.info("Client session: {}", webSocketClient.session)
    }

    @Test
    fun canReceiveMessagesWithClient(){
        webSocketClient.send("Hello")
        
        Awaitility.await().timeout(Duration.ofSeconds(10)).untilAsserted{
            val messages = webSocketClient.observedMessages
            log.info("Observed Messages {} - {}", messages.size, messages)
            Assertions.assertEquals("Connected!", messages.poll())
            Assertions.assertEquals("Not supported => (Hello)", messages.poll())
        }
        webSocketClient.close()
    }

    @Test
    fun canSendReactively(){
        log.info("Sent {}", webSocketClient.sendReactive("Hello").blockingGet())

        Awaitility.await().timeout(Duration.ofSeconds(10)).untilAsserted{
            val messages = webSocketClient.observedMessages
            log.info("Observed Messages {} - {}", messages.size, messages)
            Assertions.assertEquals("Connected!", messages.poll())
            Assertions.assertEquals("Not supported => (Hello)", messages.poll())
        }
        webSocketClient.close()
    }

}
