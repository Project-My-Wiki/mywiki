package com.yhproject.mywiki.auth

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val oAuthUserService: OAuthUserService,
    private val googleTokenService: GoogleTokenService,
    private val jwtProvider: JwtProvider
) {
    data class LoginRequest(val provider: String, val idToken: String)

    data class LoginResponse(val accessToken: String)

    /**
     * 모바일 앱 소셜 로그인 엔드포인트.
     *
     * 클라이언트(모바일 앱)에서 Google SDK로 직접 로그인하여 획득한 ID Token을 전달합니다.
     * 백엔드는 ID Token을 로컬 검증(서명, 만료, aud)하고 자체 JWT를 발급합니다.
     *
     * @param request provider ("google"), idToken (Google ID Token)
     * @return 자체 발급 JWT accessToken
     */
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        logger.info { "소셜 로그인 요청: provider=${request.provider}" }

        val userInfo = when (request.provider.lowercase()) {
            "google" -> googleTokenService.verify(request.idToken)
            else -> throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "지원하지 않는 OAuth2 프로바이더입니다: ${request.provider}"
            )
        }

        val user = oAuthUserService.saveOrUpdate(userInfo)
        val accessToken = jwtProvider.generateToken(user.id)

        logger.info { "소셜 로그인 성공: userId=${user.id}, provider=${request.provider}" }
        return ResponseEntity.ok(LoginResponse(accessToken))
    }
}
