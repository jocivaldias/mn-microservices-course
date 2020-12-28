package com.jocivaldias

import io.micronaut.scheduling.annotation.Scheduled
import io.micronaut.websocket.WebSocketBroadcaster
import java.math.BigDecimal
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Singleton

@Singleton
class PricePush(val broadcaster: WebSocketBroadcaster) {

    @Scheduled(fixedDelay = "1s")
    fun push(){
        broadcaster.broadcastSync(
            PriceUpdate("AMZN", randomValue())
        )
    }

    private fun randomValue(): BigDecimal{
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1500.0, 2000.0))
    }

}