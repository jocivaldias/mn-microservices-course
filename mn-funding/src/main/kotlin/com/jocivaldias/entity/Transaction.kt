package com.jocivaldias.entity

import io.micronaut.core.annotation.Introspected
import io.micronaut.data.annotation.AutoPopulated
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.model.naming.NamingStrategies
import java.math.BigDecimal
import java.util.UUID


@MappedEntity(value = "transactions", namingStrategy = NamingStrategies.UnderScoreSeparatedLowerCase::class)
@Introspected
data class Transaction (
    @field:Id
    @GeneratedValue(GeneratedValue.Type.IDENTITY)
    var transactionId: Long? = null,
    var user: UUID,
    var symbol: String,
    var modification: BigDecimal
)