package com.jocivaldias

import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.Topic
import io.reactivex.Flowable
import org.apache.kafka.clients.producer.RecordMetadata

@KafkaClient(
    id="external-price-update-producer",
    batch = true
)
interface PriceUpdateProducer {

    @Topic("price-update")
    fun send(prices: List<PriceUpdate>): Flowable<RecordMetadata>

}