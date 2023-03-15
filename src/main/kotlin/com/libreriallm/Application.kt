package com.libreriallm

import com.libreriallm.plugins.configureMonitoring
import com.libreriallm.plugins.configureRouting
import com.libreriallm.plugins.configureSerialization
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.request.*
import kotlinx.html.*
import kotlinx.serialization.*

val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}

    fun Application.main(httpClient: HttpClient = applicationHttpClient) {
        install(Sessions) {
            cookie<UserSession>("user_session")
        }

        configureRouting()
        configureMonitoring()
        configureSerialization()

        val redirects = mutableMapOf<String, String>()
        install(Authentication) {
            oauth("auth-oauth-google") {
                urlProvider = { "http://localhost:8080/callback" }
                providerLookup = {
                    OAuthServerSettings.OAuth2ServerSettings(
                        name = "google",
                        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                        accessTokenUrl = "https://accounts.google.com/o/oauth2/token",
                        requestMethod = HttpMethod.Post,
                        clientId = "972232198523-r25fta7dgbdjoc8pj0mblnakd61dtceg.apps.googleusercontent.com",
                        clientSecret = "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",
                        defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile"),
                        extraAuthParameters = listOf("access_type" to "offline"),
                        onStateCreated = { call, state ->
                            redirects[state] = call.request.queryParameters["redirectUrl"]!!
                        }
                    )
                }
                client = httpClient
            }
        }
        routing {
            authenticate("auth-oauth-google") {
                get("/login") {
                    // Redirects to 'authorizeUrl' automatically
                }

                get("/callback") {
                    val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()
                    call.sessions.set(UserSession(principal!!.state!!, principal.accessToken))
                    val redirect = redirects[principal.state!!]
                    call.respondRedirect(redirect!!)
                }
            }
            get("/") {
                call.respondHtml {
                    body {
                        p {
                            a("/login") { +"Login with Google" }
                        }
                    }
                }
            }
            get("/{path}") {
                val userSession: UserSession? = call.sessions.get()
                if (userSession != null) {
                    val userInfo: UserInfo = httpClient.get("https://www.googleapis.com/oauth2/v2/userinfo") {
                        headers {
                            append(HttpHeaders.Authorization, "Bearer ${userSession.token}")
                        }
                    }.body()
                    call.respondText("Hello, ${userInfo.name}!")
                } else {
                    val redirectUrl = URLBuilder("http://0.0.0.0:8080/login").run {
                        parameters.append("redirectUrl", call.request.uri)
                        build()
                    }
                    call.respondRedirect(redirectUrl)
                }
            }
        }
    }
data class UserSession(val state: String, val token: String)
@Serializable
data class UserInfo(
    val id: String,
    val name: String,
    @SerialName("given_name") val givenName: String,
    @SerialName("family_name") val familyName: String,
    val picture: String,
    val locale: String
)