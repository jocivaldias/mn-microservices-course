package com.jocivaldias.broker.error

data class CustomError(
        var status: Int,
        var error: String,
        var message: String,
        var path: String
)