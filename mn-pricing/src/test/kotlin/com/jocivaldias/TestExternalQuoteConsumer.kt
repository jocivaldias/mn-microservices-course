package com.jocivaldias

import io.micronaut.configuration.kafka.annotation.*
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.core.util.CollectionUtils
import io.micronaut.core.util.StringUtils
import org.awaitility.Awaitility
import org.junit.jupiter.api.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.math.BigDecimal
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.IntStream
import javax.inject.Singleton

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Testcontainers
class TestExternalQuoteConsumer {

    private val log: Logger = LoggerFactory.getLogger(TestExternalQuoteConsumer::class.java)
    
    @Container
    val kafka: KafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"))

    lateinit var context: ApplicationContext

    val random: ThreadLocalRandom = ThreadLocalRandom.current()


    @BeforeAll
    fun startKafka(){
        var teste: BigDecimal = BigDecimal.ONE

        kafka.start()
        log.debug("Bootstrap servers: {}", kafka.bootstrapServers)

        val mapOf = CollectionUtils.mapOf(
            "kafka.bootstrap.servers", kafka.bootstrapServers,
            "TestExternalQuoteConsumer", StringUtils.TRUE
        ) as Map<String, Any>

        context = ApplicationContext.run(
            mapOf,
            Environment.TEST)

    }

    @AfterAll
    fun stopKafka(){
        kafka.stop()
    }

    @Test
    fun consumingPriceUpdatesWorkCorrectly() {
        val observer = context.getBean(PriceUpdateObserver::class.java)

        val quoteProducer = context.getBean(TestScopedExternalQuoteProducer::class.java)

        IntStream.range(0, 4).forEach {
            quoteProducer.send(ExternalQuote(
                "TEST-$it",
                randomValue(),
                randomValue()
            ))
        }

        Awaitility.await().untilAsserted{
            Assertions.assertEquals(4, observer.inspected.size)
        }
    }

    private fun randomValue(): BigDecimal {
        return BigDecimal.valueOf(random.nextDouble(0.0, 1000.0))
    }

    @Singleton
    @Requirements(
        Requires(env = [Environment.TEST]),
        Requires(property = "TestExternalQuoteConsumer", value = StringUtils.TRUE)
    )
    class PriceUpdateObserver  {
        var inspected = ArrayList<PriceUpdate>()

        private val log: Logger = LoggerFactory.getLogger(PriceUpdateObserver::class.java)

        @KafkaListener(
            clientId = "price-update-observer",
            offsetReset = OffsetReset.EARLIEST
        )
        @Topic("price-update")
        fun receive(priceUpdates: List<PriceUpdate>){
            log.debug("Consumed: {}", priceUpdates)
            inspected.addAll(priceUpdates)
        }
    }

    @KafkaClient
    @Requirements(
        Requires(env = [Environment.TEST]),
        Requires(property = "TestExternalQuoteConsumer", value = StringUtils.TRUE)
    )
    interface TestScopedExternalQuoteProducer {

        @Topic("external-quotes")
        fun send(externalQuote: ExternalQuote )

    }

}
