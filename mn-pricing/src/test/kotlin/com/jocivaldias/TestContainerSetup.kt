package com.jocivaldias

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import javax.inject.Inject

@Testcontainers
class TestContainerSetup {

    private val log: Logger = LoggerFactory.getLogger(TestContainerSetup::class.java)
    
    @Container
    val kafka: KafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"))


    @Test
    fun testItWorks() {
        kafka.start()
        log.debug("Bootstrap servers: {}", kafka.bootstrapServers)
        kafka.stop()
    }

}
