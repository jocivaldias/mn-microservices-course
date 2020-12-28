package com.jocivaldias.auth.jwt

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class AuthenticationProviderUserPassword: AuthenticationProvider{

    private val log: Logger = LoggerFactory.getLogger(AuthenticationProviderUserPassword::class.java)

    override fun authenticate(httpRequest: HttpRequest<*>?, authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse> {
        return Flowable.create({ emitter ->
            val identity = authenticationRequest?.identity
            val secret = authenticationRequest?.secret
            log.debug("User {} tries to login...", identity)

            if ("my-user".equals(identity) && "secret".equals(secret)){
                emitter.onNext(UserDetails(identity as String?, ArrayList()))
                emitter.onComplete()
                return@create
            }
            emitter.onError(AuthenticationException(AuthenticationFailed("Wrong username and password")))
        }, BackpressureStrategy.ERROR)
    }

}