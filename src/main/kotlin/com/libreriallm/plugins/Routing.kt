package com.libreriallm.plugins

import com.libreriallm.rutas.rutasLibro
import com.libreriallm.rutas.rutasUsuario
import io.ktor.server.routing.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        // Importante llamar a las rutas personalizadas desde aquí.
        rutasLibro()
        rutasUsuario()
        // Static plugin. Try to access `/static/index.html`
        static {
            resources("static")
        }
    }
}
