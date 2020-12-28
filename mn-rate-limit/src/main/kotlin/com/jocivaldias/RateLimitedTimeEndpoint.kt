package com.jocivaldias

import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Clock
import java.time.LocalTime

@Controller("/time")
class RateLimitedTimeEndpoint(
    private val redisConnection: StatefulRedisConnection<String, String>
) {

    private val QUOTE_PER_MINUTE: Int = 10

    private val log: Logger = LoggerFactory.getLogger(RateLimitedTimeEndpoint::class.java)

    @ExecuteOn(TaskExecutors.IO)
    @Get("/")
    fun time(): String{
        return getTime("EXAMPLE::TIME", LocalTime.now())
    }

    @ExecuteOn(TaskExecutors.IO)
    @Get("/utc")
    fun utc(): String{
        return getTime("EXAMPLE::UTC", LocalTime.now(Clock.systemUTC()))
    }

    private fun getTime(key: String, now: LocalTime): String {
        val value = redisConnection.sync().get(key)
        val currentQuota = value?.toInt() ?: 0

        if (currentQuota >= QUOTE_PER_MINUTE) {
            val errorMessage = String.format("Rate limit reached %s %s/%s", key, currentQuota, QUOTE_PER_MINUTE)
            log.info(errorMessage)
            return errorMessage
        }
        log.info("Current quota {} in {}/{}", key, currentQuota, QUOTE_PER_MINUTE)
        increaseCurrentQuota(key)

        return now.toString()
    }


    private fun increaseCurrentQuota(key: String) {
        val commands: RedisCommands<String, String> = redisConnection.sync()
        commands.multi()
        commands.incrby(key, 1)
        val remainingSeconds = 60 - LocalTime.now().second
        commands.expire(key, remainingSeconds.toLong())
        commands.exec()
    }

}