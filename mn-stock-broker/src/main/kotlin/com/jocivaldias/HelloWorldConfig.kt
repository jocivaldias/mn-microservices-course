package com.jocivaldias

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("hello.from")
class HelloWorldConfig {
    var de: String? = null
    var en: String? = null
}
