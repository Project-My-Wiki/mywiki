package com.yhproject.mywiki.auth

import io.jsonwebtoken.ExpiredJwtException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class JwtProviderTest {

    private lateinit var jwtProvider: JwtProvider
    private val secretKeyString = "mywiki-test-secret-key-must-be-at-least-256-bits-long"
    private val expirationMs = 3600000L // 1 hour

    @BeforeEach
    fun setUp() {
        jwtProvider = JwtProvider(secretKeyString, expirationMs)
    }

    @Test
    @DisplayName("JWT 토큰 생성 및 파싱 테스트")
    fun `generate and parse token`() {
        // given
        val userId = 1L

        // when
        val token = jwtProvider.generateToken(userId)

        // then
        assertThat(token).isNotBlank()

        val claims = jwtProvider.getClaims(token)
        assertThat(claims.subject).isEqualTo(userId.toString())
        // 개인정보 클레임이 포함되지 않아야 한다
        assertThat(claims["name"]).isNull()
        assertThat(claims["email"]).isNull()
        assertThat(claims["role"]).isNull()
    }

    @Test
    @DisplayName("유효한 JWT 토큰 검증 테스트")
    fun `validate valid token`() {
        // given
        val token = jwtProvider.generateToken(1L)

        // when
        val isValid = jwtProvider.validateToken(token)

        // then
        assertThat(isValid).isTrue()
    }

    @Test
    @DisplayName("잘못된 형식의 서명을 가진 토큰 검증 실패 테스트")
    fun `validate invalid signature token`() {
        // given
        val token = jwtProvider.generateToken(1L)
        val invalidToken = token + "invalid"

        // when
        val isValid = jwtProvider.validateToken(invalidToken)

        // then
        assertThat(isValid).isFalse()
    }

    @Test
    @DisplayName("만료된 토큰 검증 실패 테스트")
    fun `validate expired token`() {
        // given
        // 만료 시간을 0ms로 설정하여 즉시 만료되는 프로바이더 생성
        val expiredJwtProvider = JwtProvider(secretKeyString, 0)
        val token = expiredJwtProvider.generateToken(1L)

        // 만료된 토큰은 validateToken 에서 false를 반환해야 합니다.
        // when
        val isValid = expiredJwtProvider.validateToken(token)

        // then
        assertThat(isValid).isFalse()

        // 추가로 getClaims 호출 시 ExpiredJwtException 이 발생하는지 검증
        assertThatThrownBy { expiredJwtProvider.getClaims(token) }
                .isInstanceOf(ExpiredJwtException::class.java)
    }
}
