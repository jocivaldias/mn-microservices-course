package com.jocivaldias.broker

import com.jocivaldias.broker.dto.QuoteDTO
import com.jocivaldias.broker.dto.SymbolDTO
import com.jocivaldias.broker.error.CustomError
import com.jocivaldias.broker.repository.InMemoryStore
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal


@MicronautTest
class QuotesControllerTest {

    var inMemoryStore: InMemoryStore = mockk()
    var quotesController: QuotesController = QuotesController(inMemoryStore)

    @Test
    fun `given Symbol should return status OK`() {
        val quote = QuoteDTO(
                SymbolDTO("Example"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        )

        // given
        every {
            inMemoryStore.fetchQuote("Example")
        } returns quote

        // when
        val response = quotesController.getQuotes("Example")

        // then
        assertEquals(response.status, HttpStatus.OK)
    }

    @Test
    fun `given invalid Symbol should return status NOT_FOUND`() {
        // given
        every{
            inMemoryStore.fetchQuote("Example")
        } returns null

        // when
        val response = quotesController.getQuotes("Example")
        val responseBody = response.getBody(CustomError::class.java)

        // then
        assertTrue(responseBody.isPresent)
        assertEquals(404, responseBody.get().status)
        assertEquals("NOT_FOUND", responseBody.get().error)
        assertEquals("quote for symbol not found", responseBody.get().message)
        assertEquals("/quotes/Example", responseBody.get().path)
        assertEquals(HttpStatus.NOT_FOUND, response.status)
    }



}