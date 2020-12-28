package com.jocivaldias.broker.entity

import java.math.BigDecimal
import javax.persistence.*


@Entity(name="quote")
@Table(name="quotes", schema = "mn")
data class Quote (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @ManyToOne(targetEntity = Symbol::class)
        @JoinColumn(name = "symbol", referencedColumnName = "value")
        val symbol: Symbol,

        val bid: BigDecimal,

        val ask: BigDecimal,

        @Column(name = "last_price")
        val lastPrice: BigDecimal,

        val volume: BigDecimal
)