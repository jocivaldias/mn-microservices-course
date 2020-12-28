package com.jocivaldias.broker.repository

import com.jocivaldias.broker.entity.Quote
import com.jocivaldias.broker.entity.Symbol
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.concurrent.ThreadLocalRandom
import java.util.stream.Stream
import javax.inject.Singleton

/**
 * Used to insert data at startup (for testing purposes)
 */
@Singleton
@Requires(notEnv = [Environment.TEST])
class TestDataProvider(
        private val symbolRepository: SymbolRepository,
        private val quotesRepository: QuotesRepository
){

    private val current: ThreadLocalRandom = ThreadLocalRandom.current()
    private val log: Logger = LoggerFactory.getLogger(TestDataProvider::class.java)
    
    @EventListener
    fun init(startupEvent: StartupEvent) {
        if(symbolRepository.findAll().isEmpty()){
            log.info("Adding test data for symbol")
            Stream.of("AAPL", "AMZN", "FB", "TSLA")
                    .map { Symbol(it) }
                    .forEach(symbolRepository::save)
        }
        if(quotesRepository.findAll().isEmpty()){
            log.info("Adding test data for quotes")
            symbolRepository.findAll().forEach {
                var quote: Quote = Quote(
                        symbol = it,
                        bid = randomValue(),
                        ask = randomValue(),
                        lastPrice = randomValue(),
                        volume = randomValue()
                )

                quotesRepository.save(quote)


            }
        }
    }

    private fun randomValue(): BigDecimal {
        return BigDecimal.valueOf(current.nextDouble(1.0, 100.0));
    }

}