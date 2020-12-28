package com.jocivaldias.broker.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "Symbol", description = "Abbreviation to uniquely identify public trades shares of a stock.")
data class SymbolDTO (
    @field:Schema(description = "symbol value", minLength = 1, maxLength = 5) val value: String
)
