package com.jocivaldias.broker.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity(name = "symbol")
@Table(name = "symbols", schema = "mn")
data class Symbol(
        @Id
        val value: String? = null
)