package com.jocivaldias.broker

import com.jocivaldias.broker.entity.Symbol
import com.jocivaldias.broker.repository.SymbolRepository
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.reactivex.Single
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/markets")
class MarketsController(
        private val symbolRepository: SymbolRepository
) {

    @Operation(summary = "Returns all available markets")
    @ApiResponse(content = [Content(mediaType = MediaType.APPLICATION_JSON)])
    @Tag(name = "markets")
    @Get("/")
    fun all(): Single<List<Symbol>> {
        return Single.just(symbolRepository.findAll())
    }

}