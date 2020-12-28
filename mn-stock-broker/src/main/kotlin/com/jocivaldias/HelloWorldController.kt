package com.jocivaldias

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/hello")
class HelloWorldController (
        private val helloWorldService: HelloWorldService,
        private val helloWorldConfig: HelloWorldConfig,
        ){

    @Get("/")
    @Produces(MediaType.TEXT_PLAIN)
    fun hello(): String{
        return "hello"
    }

    @Get("/service")
    @Produces(MediaType.TEXT_PLAIN)
    fun helloFromService(): String{
        return helloWorldService.sayHi()
    }

    @Get("/de")
    @Produces(MediaType.TEXT_PLAIN)
    fun helloFromConfigDe(): String?{
        return helloWorldConfig.de
    }

    @Get("/en")
    @Produces(MediaType.TEXT_PLAIN)
    fun helloFromConfigEn(): String?{
        return helloWorldConfig.en
    }

    @Get("/json")
    fun json(): Greeting {
        return Greeting()
    }

}