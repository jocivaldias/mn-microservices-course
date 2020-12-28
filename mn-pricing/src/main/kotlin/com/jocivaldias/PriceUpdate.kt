package com.jocivaldias

import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal

@Introspected
class PriceUpdate(
    val symbol: String,
    val lastPrice: BigDecimal
) {

    override fun toString(): String {
        return "PriceUpdate(symbol='$symbol', lastPrice=$lastPrice)"
    }
}
