package com.jocivaldias.broker

import com.jocivaldias.broker.dto.SymbolDTO
import com.jocivaldias.broker.dto.WatchListDTO
import com.jocivaldias.broker.repository.InMemoryAccountStore
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.collections.ArrayList


@MicronautTest
class WatchListDTOControllerTest {

    var inMemoryAccountStore: InMemoryAccountStore = mockk()
    var watchListController: WatchListController = WatchListController(inMemoryAccountStore)

    var TEST_ACCOUNT_ID: UUID = watchListController.accountUUID

    @Test
    fun `given Account return empty WatchList `() {
        val emptyWatchListDTO: WatchListDTO = WatchListDTO(ArrayList())

        // given
        every {
            inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID)
        } returns emptyWatchListDTO

        // when
        val response = watchListController.get()
        val responseBody = response.getBody(WatchListDTO::class.java)

        // then
        assertTrue(responseBody.isPresent)
        assertTrue(responseBody.get().symbolDTOS.isEmpty())
    }

    @Test
    fun `given Account return WatchList`() {
        val symbolsList = Stream.of("APPL", "AMZN", "NFLX")
                .map { SymbolDTO(it) }
                .collect(Collectors.toList())
        val watchListDTO: WatchListDTO = WatchListDTO(symbolsList)

        // given
        every {
            inMemoryAccountStore.getWatchList(TEST_ACCOUNT_ID)
        } returns watchListDTO

        // when
        val response = watchListController.get()

        val responseBody = response.getBody(WatchListDTO::class.java)

        // then
        assertTrue(responseBody.isPresent)
        assertEquals(3, responseBody.get().symbolDTOS.size)
    }

    @Test
    fun `given WatchList update Account WatchList`() {
        val symbolsList = Stream.of("APPL", "AMZN", "NFLX")
                .map { SymbolDTO(it) }
                .collect(Collectors.toList())
        val watchListDTO: WatchListDTO = WatchListDTO(symbolsList)

        // given
        every {
            inMemoryAccountStore.updateWatchList(TEST_ACCOUNT_ID, watchListDTO)
        } returns watchListDTO

        // when
        val response = watchListController.update(watchListDTO)

        // then
        assertEquals(HttpStatus.OK, response.status)
    }

    @Test
    fun `given valid accountId when delete WatchList then return OK`() {
        // given
        every {
            inMemoryAccountStore.delete(TEST_ACCOUNT_ID)
        } returns Unit

        // when
        val response = watchListController.delete(TEST_ACCOUNT_ID)

        // then
        assertEquals(HttpStatus.OK, response.status)
    }

}