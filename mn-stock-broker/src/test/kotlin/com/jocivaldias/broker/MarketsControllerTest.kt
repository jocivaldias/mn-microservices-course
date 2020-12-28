package com.jocivaldias.broker

import com.jocivaldias.broker.dto.SymbolDTO
import com.jocivaldias.broker.repository.InMemoryStore
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.stream.Collectors


@MicronautTest
class MarketsControllerTest {

//    @MockBean(InMemoryStore::class)
//    fun inMemoryStoreMock(): InMemoryStore = Mockito.mock(InMemoryStore::class.java)
//
//    @Inject
    var inMemoryStore: InMemoryStore = mockk()

    var marketsController: MarketsController = MarketsController(inMemoryStore)

    @Test
    fun `should return correct List of Symbols`() {
        var symbolDTOS: List<SymbolDTO> = listOf("APPL", "AMZN", "FB")
                .stream()
                .map { SymbolDTO(it) }
                .collect(Collectors.toList())

        every {
            inMemoryStore.getAllSymbols()
        } returns symbolDTOS

        // when
        val response = marketsController.all()
        // then
        assertEquals(response, symbolDTOS)
    }






}