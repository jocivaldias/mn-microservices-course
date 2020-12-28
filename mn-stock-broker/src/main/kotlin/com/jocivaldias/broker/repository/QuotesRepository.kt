package com.jocivaldias.broker.repository

import com.jocivaldias.broker.entity.Quote
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.Slice
import java.math.BigDecimal

@Repository
interface QuotesRepository: JpaRepository<Quote, Long> {
    fun findBySymbolValue(symbol: String): Quote?
    fun listOrderByVolumeDesc(): List<Quote>
    fun listOrderByVolumeAsc(): List<Quote>
    fun findByVolumeGreaterThanOrderByVolumeAsc(volume: BigDecimal): List<Quote>
    fun findByVolumeGreaterThan(volume: BigDecimal, pageable: Pageable): Page<Quote>
    fun list(pageable: Pageable): Slice<Quote>
}