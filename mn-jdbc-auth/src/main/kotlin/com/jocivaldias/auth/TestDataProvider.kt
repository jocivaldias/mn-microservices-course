package com.jocivaldias.auth

import com.jocivaldias.auth.entity.User
import com.jocivaldias.auth.repository.UserRepository
import io.micronaut.context.event.StartupEvent
import io.micronaut.runtime.event.annotation.EventListener
import javax.inject.Singleton

@Singleton
class TestDataProvider(
        private val userRepository: UserRepository
) {

    @EventListener
    fun init(event: StartupEvent){
        if(userRepository.findByEmail("alice@example.com") == null){
            userRepository.save(User(
                    email = "alice@example.com",
                    password = "secret"
            ))
        }
    }


}