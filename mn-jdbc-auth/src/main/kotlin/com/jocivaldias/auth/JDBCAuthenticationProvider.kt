package com.jocivaldias.auth

import com.jocivaldias.auth.repository.UserRepository
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import org.reactivestreams.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class JDBCAuthenticationProvider(
        val userRepository: UserRepository
): AuthenticationProvider {

    val log: Logger = LoggerFactory.getLogger(JDBCAuthenticationProvider::class.java)

    override fun authenticate(httpRequest: HttpRequest<*>?, authenticationRequest: AuthenticationRequest<*, *>?): Publisher<AuthenticationResponse> {
        return Flowable.create( {emitter ->
            val identity = authenticationRequest?.identity as String
            log.debug("User {} tries to login...", identity)

            val user = userRepository.findByEmail(identity)
            if(user != null){
                log.debug("Found user: {}", user.email)
                val secret = authenticationRequest.secret
                if(secret == user.password){
                    log.debug("User logged in.")
                    val attributes: HashMap<String, Any> = HashMap()
                    attributes["hair_color"] = "brown"
                    attributes["language"] = "en"

                    val userDetails = UserDetails(identity, listOf("ROLE_USER"), attributes)
                    emitter.onNext(userDetails)
                    emitter.onComplete()
                    return@create
                } else {
                    log.debug("Wrong password privader for user {}", identity)
                }
            } else {
                log.debug("No user found for this email: {}", identity)
                emitter.onError(AuthenticationException(AuthenticationFailed("Wrong username or password")))
            }
        }, BackpressureStrategy.ERROR)
    }
}