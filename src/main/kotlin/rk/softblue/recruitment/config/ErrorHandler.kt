package rk.softblue.recruitment.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import rk.softblue.recruitment.model.ErrorResponse

fun Application.errorHandling() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            when (throwable) {
                is IllegalArgumentException -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(
                            "${throwable.message}",
                            HttpStatusCode.BadRequest.value,
                            HttpStatusCode.BadRequest.description
                        )
                    )
                }

                is NotFoundException -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(
                            "${throwable.message}",
                            HttpStatusCode.NotFound.value,
                            HttpStatusCode.NotFound.description
                        )
                    )
                }
            }
        }

        status(
            HttpStatusCode.InternalServerError,
            HttpStatusCode.NotFound,
            HttpStatusCode.BadRequest
        ) { call, statusCode ->
            when (statusCode) {
                HttpStatusCode.InternalServerError -> {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        ErrorResponse("Internal server error", statusCode.value, statusCode.description)
                    )
                }

                HttpStatusCode.BadRequest -> {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("Request can't be handled.", statusCode.value, statusCode.description)
                    )
                }

                HttpStatusCode.NotFound -> {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(
                            "Endpoint not found. Check spelling.",
                            statusCode.value,
                            statusCode.description
                        )
                    )
                }
            }
        }
    }
}