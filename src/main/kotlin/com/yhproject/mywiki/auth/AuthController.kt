package com.yhproject.mywiki.auth

import com.yhproject.mywiki.domain.user.Role
import com.yhproject.mywiki.domain.user.User
import com.yhproject.mywiki.domain.user.UserRepository
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/api/auth")
class AuthController(
        private val userRepository: UserRepository,
        private val jwtProvider: JwtProvider
) {

        data class LoginRequest(val provider: String, val token: String)

        data class LoginResponse(val accessToken: String)

        @PostMapping("/login")
        fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
                val userInfo = fetchUserInfoFromProvider(request.provider, request.token)

                val email =
                        userInfo["email"] as? String
                                ?: throw IllegalArgumentException("Email not found from provider")
                val name = userInfo["name"] as? String ?: "Unknown"
                val providerId = userInfo["id"] as? String ?: ""

                val user =
                        userRepository.findByEmail(email)?.update(name)
                                ?: User(
                                        name = name,
                                        email = email,
                                        role = Role.USER,
                                        provider = request.provider,
                                        providerId = providerId
                                )
                val savedUser = userRepository.save(user)

                val jwtToken = jwtProvider.generateToken(savedUser.id)

                return ResponseEntity.ok(LoginResponse(jwtToken))
        }

        @Suppress("UNCHECKED_CAST")
        private fun fetchUserInfoFromProvider(provider: String, token: String): Map<String, Any> {
                val restTemplate = RestTemplate()
                val headers = HttpHeaders()
                headers.setBearerAuth(token)
                val entity = HttpEntity<String>("", headers)

                return try {
                        when (provider.lowercase()) {
                                "google" -> {
                                        val response =
                                                restTemplate.exchange(
                                                        "https://www.googleapis.com/oauth2/v3/userinfo",
                                                        HttpMethod.GET,
                                                        entity,
                                                        Map::class.java
                                                )
                                        val body = response.body as Map<String, Any>
                                        mapOf(
                                                "id" to body["sub"].toString(),
                                                "email" to body["email"].toString(),
                                                "name" to body["name"].toString()
                                        )
                                }
                                "kakao" -> {
                                        val response =
                                                restTemplate.exchange(
                                                        "https://kapi.kakao.com/v2/user/me",
                                                        HttpMethod.GET,
                                                        entity,
                                                        Map::class.java
                                                )
                                        val body = response.body as Map<String, Any>
                                        val kakaoAccount =
                                                body["kakao_account"] as? Map<String, Any>
                                        val profile =
                                                kakaoAccount?.get("profile") as? Map<String, Any>

                                        val email = kakaoAccount?.get("email")?.toString() ?: ""
                                        val nickname = profile?.get("nickname")?.toString() ?: ""

                                        mapOf(
                                                "id" to body["id"].toString(),
                                                "email" to email,
                                                "name" to nickname
                                        )
                                }
                                else ->
                                        throw IllegalArgumentException(
                                                "Unsupported provider: $provider"
                                        )
                        }
                } catch (e: Exception) {
                        throw IllegalArgumentException(
                                "Failed to fetch user info from provider: ${e.message}"
                        )
                }
        }
}
