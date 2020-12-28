package com.jocivaldias

import io.micronaut.runtime.Micronaut.build
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@OpenAPIDefinition(
		info = Info(
				title = "mn-stock-broker",
				version = "0.1",
				description = "Udemy Micronaut Course",
				license = License(name = "MIT")
		)
)
object  Application {
	@JvmStatic
	fun main(args: Array<String>) {
		val log: Logger = LoggerFactory.getLogger("main")

		val ctx = build()
				.args(*args)
				.packages("com.jocivaldias")
				.start()

		log.info(ctx.getBean(HelloWorldService::class.java).sayHi())
	}
}

