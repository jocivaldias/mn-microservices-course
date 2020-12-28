package com.jocivaldias

import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class HelloWorldService {

    private val LOG: Logger = LoggerFactory.getLogger(HelloWorldService::class.java)

    private val greetings: String = "Hello from service"

    fun sayHi(): String{
        return greetings
    }

    @EventListener
    fun onStartup(startupEvent: StartupEvent){
        LOG.debug("Startup {}", HelloWorldService::class.simpleName)
    }

}
