package com.jocivaldias.broker

import com.jocivaldias.broker.dto.WatchListDTO
import com.jocivaldias.broker.repository.InMemoryAccountStore
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@MicronautTest
class WatchListDTOControllerReactiveTest (
        watchListController: WatchListControllerReactive
){

    var inMemoryAccountStore: InMemoryAccountStore = mockk()

    private var testAccountId: UUID = watchListController.accountUUID

    @Inject
    @field:Client("/accounts/watchlist-reactive")
    lateinit var client: RxHttpClient;

    @Test
    fun `given Account return empty WatchList `() {
        val emptyWatchListDTO: WatchListDTO = WatchListDTO(ArrayList())

        // given
        every {
            inMemoryAccountStore.getWatchList(testAccountId)
        } returns emptyWatchListDTO

        // when
        val request: HttpRequest<Any> = HttpRequest.GET("/")
        val response: Single<WatchListDTO> = client.retrieve(request, WatchListDTO::class.java).singleOrError()

        // then
        assertTrue(response.blockingGet().symbolDTOS.isEmpty())
    }

    @Test
    fun `given Account return empty WatchList as Single `() {
        val emptyWatchListDTO: WatchListDTO = WatchListDTO(ArrayList())

        // given
        every {
            inMemoryAccountStore.getWatchList(testAccountId)
        } returns emptyWatchListDTO

        // when
        val request: HttpRequest<Any> = HttpRequest.GET("/single")
        val response: Single<WatchListDTO> = client.retrieve(request, WatchListDTO::class.java).singleOrError()

        // then
        assertTrue(response.blockingGet().symbolDTOS.isEmpty())
    }

}