package com.jocivaldias

import com.jocivaldias.entity.Transaction
import com.jocivaldias.repository.TransactionRepository
import io.micronaut.scheduling.annotation.Scheduled
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Singleton

@Singleton
class DummyDataProvider(
    private val transactionRepository: TransactionRepository
) {

    private val log: Logger = LoggerFactory.getLogger(DummyDataProvider::class.java)

    @Scheduled(fixedDelay = "1s")
    fun generate(){
        transactionRepository.save(Transaction(
            user = UUID.randomUUID(),
            symbol = "SYMBOL",
            modification = randomValue()
        ))
        log.info("Content {}", transactionRepository.findAll().size)
    }

    private fun randomValue(): BigDecimal {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1.0, 100.0))
    }


}