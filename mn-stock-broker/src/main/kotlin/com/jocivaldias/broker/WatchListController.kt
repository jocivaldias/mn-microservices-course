package com.jocivaldias.broker

import com.jocivaldias.broker.dto.WatchListDTO
import com.jocivaldias.broker.repository.InMemoryAccountStore
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.annotation.security.PermitAll

@Secured(SecurityRule.IS_AUTHENTICATED)
@PermitAll
@Controller("/accounts/watchlist")
class WatchListController(
        private val inMemoryAccountStore: InMemoryAccountStore
) {

    val log: Logger = LoggerFactory.getLogger(WatchListControllerReactive::class.java)
    val accountUUID = UUID.randomUUID()

    @Get("/")
    fun get(): HttpResponse<WatchListDTO> {
        log.debug("getWatchList - {}", Thread.currentThread().name)
        return HttpResponse.ok(inMemoryAccountStore.getWatchList(accountUUID))
    }

    @Put("/")
    fun update(@Body watchListDTO: WatchListDTO): HttpResponse<WatchListDTO> {
        return HttpResponse.ok(inMemoryAccountStore.updateWatchList(accountUUID, watchListDTO))
    }

    @Delete("/{accountId}")
    fun delete(@PathVariable("accountId") accountId: UUID): HttpResponse<WatchListDTO>{
        inMemoryAccountStore.delete(accountId)
        return HttpResponse.ok()
    }
}