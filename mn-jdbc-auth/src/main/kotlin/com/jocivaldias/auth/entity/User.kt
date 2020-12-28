package com.jocivaldias.auth.entity

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "users")
data class User (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @field:NotEmpty
    @field:Email
    @field:Column(nullable = false, unique = true)
    val email: String,

    @field:NotEmpty
    val password: String

)