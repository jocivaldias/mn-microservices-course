package com.jocivaldias.auth

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.Principal

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/secured")
class SecuredEndpoint {

    private val log: Logger = LoggerFactory.getLogger(SecuredEndpoint::class.java)

    @Get("/status")
    fun status(principal: Principal): List<String?>{
        val details:Authentication = principal as Authentication
        log.debug("User Details: {}", details.attributes)
        return listOf<String?>(
                details.name,
                details.attributes["hair_color"] as String?,
                details.attributes["language"] as String?
        )
    }

}