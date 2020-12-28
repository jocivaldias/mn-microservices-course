package com.jocivaldias

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.scheduling.annotation.Scheduled
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Singleton

@Singleton
@Requires(notEnv = [Environment.TEST])
class EventScheduler (
    val externalQuoteProducer: ExternalQuoteProducer
) {


    private val log: Logger = LoggerFactory.getLogger(EventScheduler::class.java)
    val symbols: List<String> = listOf("APPL", "AMZN", "FB", "GOOG", "MSFI", "NFLX", "TSLA")

    @Scheduled(fixedDelay = "10s")
    fun generate(){
        val random = ThreadLocalRandom.current()
        val quote = ExternalQuote(
            symbols[random.nextInt(0, symbols.size - 1)],
            randomValue(random),
            randomValue(random)
        )
        log.debug("Generated external quote: {}", quote)
        externalQuoteProducer.send(quote.symbol, quote)
    }

    private fun randomValue(random: ThreadLocalRandom): BigDecimal {
        return BigDecimal.valueOf(random.nextDouble(0.0, 1000.0))
    }

}