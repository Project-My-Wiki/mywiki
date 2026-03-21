package com.yhproject.mywiki.auth

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger {}

/**
 * Google ID Token을 구글 공개키(JWKS)로 로컬 검증하는 컴포넌트.
 *
 * Access Token 방식과의 차이:
 * - 매 요청마다 Google API를 호출하지 않음 (로컬 서명 검증)
 * - iss, aud, exp 클레임을 자동으로 검증하여 토큰 위변조 방지
 */
@Component
class GoogleTokenService(
    @Value("\${spring.security.oauth2.client.registration.google.client-id}") private val webClientId: String
) {
    // Google 공개키는 캐시되므로, 빈(Bean) 생성 시 한 번만 인스턴스화
    private val verifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
        GoogleNetHttpTransport.newTrustedTransport(),
        GsonFactory.getDefaultInstance()
    )
        // 웹, Android, iOS 클라이언트 ID를 모두 허용
        // Google Cloud Console에서 발급받은 각 플랫폼 Client ID를 환경변수로 주입하여 추가 가능
        .setAudience(listOf(webClientId))
        .build()

    fun verify(idToken: String): OAuthUserInfo {
        val googleIdToken: GoogleIdToken = runCatching { verifier.verify(idToken) }
            .getOrElse { e ->
                logger.warn { "Google ID Token 검증 중 오류 발생: ${e.message}" }
                throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Google ID Token 검증 실패")
            } ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않거나 만료된 Google ID Token입니다")

        val payload = googleIdToken.payload
        return OAuthUserInfo(
            provider = "google",
            providerId = payload.subject,
            email = payload.email ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 정보를 확인할 수 없습니다"),
            name = payload["name"] as? String ?: ""
        )
    }
}
