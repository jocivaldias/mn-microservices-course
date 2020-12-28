package com.jocivaldias

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.Topic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

@KafkaListener(
    clientId = "mn-pricing-external-quote-consumer",
    groupId = "external-quote-consumer",
    batch = true,
    offsetReset = OffsetReset.EARLIEST
)
class ExternalQuoteConsumer(
    val priceUpdateProducer: PriceUpdateProducer
) {

    private val log: Logger = LoggerFactory.getLogger(ExternalQuoteConsumer::class.java)

    @Topic("external-quotes")
    fun receive(externalQuotes: List<ExternalQuote> ){
        log.debug("Consuming batch of external quotes {}", externalQuotes)
        // Froward price updates
        var priceUpdates = externalQuotes.stream()
            .map{PriceUpdate(it.symbol, it.lastPrice)}
            .collect(Collectors.toList())

        priceUpdateProducer.send(priceUpdates)
            .doOnError{log.error("Failed to produce: {}", it.cause)}
            .forEach{
                log.debug("Record send to topic {} on offset {}", it.topic(), it.offset())
            }
    }
}