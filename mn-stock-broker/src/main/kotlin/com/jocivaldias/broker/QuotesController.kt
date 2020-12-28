package com.jocivaldias.broker

import com.jocivaldias.broker.dto.QuoteDTO
import com.jocivaldias.broker.entity.Quote
import com.jocivaldias.broker.error.CustomError
import com.jocivaldias.broker.repository.QuotesRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.reactivex.Single
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.*
import java.util.stream.Collectors

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/quotes")
class QuotesController (private val quotesRepository: QuotesRepository){

    private val log: Logger = LoggerFactory.getLogger(QuotesController::class.java)

    @Operation(summary = "Returns all quotes")
    @ApiResponse(content = [Content(mediaType = MediaType.APPLICATION_JSON)])
    @Tag(name = "quotes")
    @Get("/")
    fun all(): Single<List<Quote>> {
        return Single.just(quotesRepository.findAll())
    }

    @Get("/{symbol}")
    @Operation(summary = "Returns a quote for the given symbol")
    @ApiResponses(
            ApiResponse(content = [Content(mediaType = "application/json")]),
            ApiResponse(responseCode = "404", description = "Invalid symbol Supplied"),
    )
    @Tag(name="quotes")
    fun getQuotes(@PathVariable symbol: String): HttpResponse<*> {
        val quote = quotesRepository.findBySymbolValue(symbol)

        if(quote == null){
            val messageError = StringJoiner("")
            val customError = CustomError(
                    HttpStatus.NOT_FOUND.code,
                    HttpStatus.NOT_FOUND.name,
                    "quote for symbol not found",
                    messageError.add("/quotes/").add(symbol).toString()
            )
            return HttpResponse.notFound(customError)
        }

        log.debug("Quote find {}", quote)
        return HttpResponse.ok(quote)
    }

    @Operation(summary = "Returns all quotes ordered by volume desc")
    @ApiResponse(content = [Content(mediaType = MediaType.APPLICATION_JSON)])
    @Tag(name = "quotes")
    @Get("/ordered/desc")
    fun orderedDesc(): List<QuoteDTO>{
        return quotesRepository.listOrderByVolumeDesc()
            .stream()
            .map { QuoteDTO(it) }
            .collect(Collectors.toList())
    }

    @Operation(summary = "Returns all quotes ordered by volume asc")
    @ApiResponse(content = [Content(mediaType = MediaType.APPLICATION_JSON)])
    @Tag(name = "quotes")
    @Get("/ordered/asc")
    fun orderedAsc(): List<QuoteDTO>{
        return quotesRepository.listOrderByVolumeAsc()
            .stream()
            .map { QuoteDTO(it) }
            .collect(Collectors.toList())
    }

    @Operation(summary = "Returns all quotes that have volume grather than volume passed")
    @ApiResponse(content = [Content(mediaType = MediaType.APPLICATION_JSON)])
    @Tag(name = "quotes")
    @Get("/volume/{volume}")
    fun volumeFilter(volume: BigDecimal): List<QuoteDTO>{
        return quotesRepository.findByVolumeGreaterThanOrderByVolumeAsc(volume)
            .stream()
            .map { QuoteDTO(it) }
            .collect(Collectors.toList())
    }

    @Operation(summary = "Returns all quotes paginated")
    @ApiResponse(content = [Content(mediaType = MediaType.APPLICATION_JSON)])
    @Tag(name = "quotes")
    @Get("/pagination{?page,volume}")
    fun volumeFilterPaginated(@QueryValue page: Int?, @QueryValue volume: BigDecimal?): Page<QuoteDTO> {
        return quotesRepository.findByVolumeGreaterThan(volume ?: BigDecimal.ZERO, Pageable.from(page ?: 0, 2))
            .stream()
            .map { QuoteDTO(it) }
            .collect(Collectors.toList())
    }

}