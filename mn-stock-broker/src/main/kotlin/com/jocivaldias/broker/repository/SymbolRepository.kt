package com.jocivaldias.broker.repository

import com.jocivaldias.broker.entity.Symbol
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface SymbolRepository: JpaRepository<Symbol, String> {
}