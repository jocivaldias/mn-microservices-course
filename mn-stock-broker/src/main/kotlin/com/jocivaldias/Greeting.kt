package com.jocivaldias

import java.math.BigDecimal
import java.time.Instant

data class Greeting (
        val myTxt: String = "Hello World",
        val id: BigDecimal = BigDecimal.ONE,
        val timeUTC: Instant = Instant.now()
)
