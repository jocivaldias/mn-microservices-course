package com.jocivaldias.auth.repository

import com.jocivaldias.auth.entity.User
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface UserRepository: CrudRepository<User, Long> {

    fun findByEmail(email: String): User?

}