package com.jocivaldias.repository

import com.jocivaldias.entity.Transaction
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface TransactionRepository: CrudRepository<Transaction, Long> {

    override fun findAll(): List<Transaction>

}