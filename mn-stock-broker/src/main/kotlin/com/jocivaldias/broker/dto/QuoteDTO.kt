package com.jocivaldias.broker.dto

import com.jocivaldias.broker.entity.Quote
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal

@Introspected
data class QuoteDTO (
        val id: Long? = null,
        val volume: BigDecimal
) {

    constructor(quote: Quote) : this(quote.id, quote.volume)
}
