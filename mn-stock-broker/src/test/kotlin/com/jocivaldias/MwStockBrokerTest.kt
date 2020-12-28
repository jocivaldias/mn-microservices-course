package com.jocivaldias
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class MwStockBrokerTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Inject
    lateinit var helloWorldService: HelloWorldService

    @Inject
    lateinit var helloWorldConfig: HelloWorldConfig

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }

    @Test
    fun testSayHi(){
        val sayHi = helloWorldService.sayHi()

        Assertions.assertEquals(sayHi, "Hello from service")
    }

    @Test
    fun testSayHiDe(){
        val de = helloWorldConfig.de
        Assertions.assertEquals(de, "Hallo")
    }

    @Test
    fun testSayHiEn(){
        val en = helloWorldConfig.en
        Assertions.assertEquals(en, "Hello")
    }

}
