package com.jocivaldias

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.Topic
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
class TestExternalQuoteProducer {

    private val log: Logger = LoggerFactory.getLogger(TestExternalQuoteProducer::class.java)
    
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
            "TestExternalQuoteProducer", StringUtils.TRUE
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
    fun producing10RecorsWorks() {
        val producer: ExternalQuoteProducer = context.getBean(ExternalQuoteProducer::class.java)

        IntStream.range(0, 10).forEach {
            producer.send(
                "TEST-$it", ExternalQuote(
                    "TEST-$it",
                    randomValue(),
                    randomValue()
                )
            )
        }

        val observer = context.getBean(ExternalQuoteObserver::class.java)
        Awaitility.await().untilAsserted{
            Assertions.assertEquals(10, observer.inspected.size)
        }
    }

    private fun randomValue(): BigDecimal {
        return BigDecimal.valueOf(random.nextDouble(0.0, 1000.0))
    }

    @Singleton
    @Requirements(
        Requires(env = [Environment.TEST]),
        Requires(property = "TestExternalQuoteProducer", value = StringUtils.TRUE)
    )
    class ExternalQuoteObserver  {
        var inspected = ArrayList<ExternalQuote>()

        private val log: Logger = LoggerFactory.getLogger(ExternalQuoteObserver::class.java)

        @KafkaListener(
            offsetReset = OffsetReset.EARLIEST
        )
        @Topic("external-quotes")
        fun receive(externalQuote: ExternalQuote){
            log.debug("Consumed: {}", externalQuote)
            inspected.add(externalQuote)
        }
    }

}
