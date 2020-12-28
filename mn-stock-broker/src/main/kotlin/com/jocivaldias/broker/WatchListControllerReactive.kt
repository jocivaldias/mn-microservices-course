package com.jocivaldias.broker

import com.jocivaldias.broker.dto.WatchListDTO
import com.jocivaldias.broker.repository.InMemoryAccountStore
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.ExecutorService
import javax.annotation.security.PermitAll
import javax.inject.Named

@Secured(SecurityRule.IS_AUTHENTICATED)
@PermitAll
@Controller("/accounts/watchlist-reactive")
class WatchListControllerReactive(
        private val inMemoryAccountStore: InMemoryAccountStore,
        @Named(TaskExecutors.IO) executor: ExecutorService
) {

    private val log: Logger = LoggerFactory.getLogger(WatchListControllerReactive::class.java)
    val accountUUID: UUID = UUID.randomUUID()
    private val scheduler: Scheduler = Schedulers.from(executor)

    @Get("/")
    @ExecuteOn(TaskExecutors.IO )
    fun get(): WatchListDTO {
        log.debug("getWatchList - {}", Thread.currentThread().name)
        return inMemoryAccountStore.getWatchList(accountUUID)
    }

    @Get("/single")
    @ExecuteOn(TaskExecutors.IO )
    fun getAsSingle(): Single<WatchListDTO> {
        log.debug("getAsSingleWatchList - {}", Thread.currentThread().name)
        return Single.fromCallable {
            inMemoryAccountStore.getWatchList(accountUUID)
        }
    }

    @Put("/")
    @ExecuteOn(TaskExecutors.IO )
    fun update(@Body watchListDTO: WatchListDTO): HttpResponse<WatchListDTO> {
        return HttpResponse.ok(inMemoryAccountStore.updateWatchList(accountUUID, watchListDTO))
    }

    @Delete("/{accountId}")
    @ExecuteOn(TaskExecutors.IO )
    fun delete(@PathVariable("accountId") accountId: UUID): HttpResponse<WatchListDTO>{
        inMemoryAccountStore.delete(accountId)
        return HttpResponse.ok()
    }
}