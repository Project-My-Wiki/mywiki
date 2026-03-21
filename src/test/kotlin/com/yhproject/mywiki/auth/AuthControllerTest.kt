package com.yhproject.mywiki.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.yhproject.mywiki.domain.user.Role
import com.yhproject.mywiki.domain.user.User
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.web.server.ResponseStatusException

@WebMvcTest(AuthController::class)
class AuthControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockitoBean
    private lateinit var oAuthUserService: OAuthUserService

    @MockitoBean
    private lateinit var googleTokenService: GoogleTokenService

    @MockitoBean
    private lateinit var jwtProvider: JwtProvider

    private val stubUser = User(
        id = 1L,
        name = "테스트 유저",
        email = "test@example.com",
        role = Role.USER,
        provider = "google",
        providerId = "google-123"
    )

    @Test
    @DisplayName("유효한 Google ID Token으로 로그인하면 200 OK와 JWT를 반환한다")
    fun `valid google id token returns jwt`() {
        // given
        val userInfo = OAuthUserInfo("google", "google-123", "test@example.com", "테스트 유저")
        whenever(googleTokenService.verify(any())).thenReturn(userInfo)
        whenever(oAuthUserService.saveOrUpdate(any())).thenReturn(stubUser)
        whenever(jwtProvider.generateToken(1L)).thenReturn("mocked.jwt.token")

        val requestBody = mapOf("provider" to "google", "idToken" to "valid-google-id-token")

        // when & then
        mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
        }.andExpect {
            status { isOk() }
            jsonPath("$.accessToken") { value("mocked.jwt.token") }
        }
    }

    @Test
    @DisplayName("유효하지 않은 Google ID Token이면 401 Unauthorized를 반환한다")
    fun `invalid google id token returns 401`() {
        // given
        whenever(googleTokenService.verify(any())).thenThrow(
            ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않거나 만료된 Google ID Token입니다")
        )

        val requestBody = mapOf("provider" to "google", "idToken" to "invalid-token")

        // when & then
        mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
        }.andExpect {
            status { isUnauthorized() }
        }
    }

    @Test
    @DisplayName("지원하지 않는 프로바이더로 요청하면 400 Bad Request를 반환한다")
    fun `unsupported provider returns 400`() {
        // given
        val requestBody = mapOf("provider" to "kakao", "idToken" to "some-token")

        // when & then
        mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
        }.andExpect {
            status { isBadRequest() }
        }
    }
}
