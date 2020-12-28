package com.jocivaldias

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaClient(
    id="external-quote-producer"
)
interface ExternalQuoteProducer {

    @Topic("external-quotes")
    fun send(@KafkaKey symbol: String, externalQuote: ExternalQuote )

}