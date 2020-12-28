package com.jocivaldias

import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal

@Introspected
class ExternalQuote (
    val symbol: String,
    val lastPrice: BigDecimal,
    val volume: BigDecimal
) {
    override fun toString(): String {
        return "ExternalQuote(symbol='$symbol', lastPrice=$lastPrice, volume=$volume)"
    }
}