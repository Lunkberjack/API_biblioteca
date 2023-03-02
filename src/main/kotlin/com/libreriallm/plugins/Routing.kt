package com.libreriallm.plugins

import com.libreriallm.rutas.libroRandom
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*

fun Application.configureRouting() {

    routing {
        libroRandom()
        // Static plugin. Try to access `/static/index.html`
        static {
            resources("static")
        }
    }
}
