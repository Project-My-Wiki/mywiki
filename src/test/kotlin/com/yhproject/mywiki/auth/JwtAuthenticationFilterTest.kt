package com.yhproject.mywiki.auth

import com.yhproject.mywiki.domain.user.Role
import com.yhproject.mywiki.domain.user.User
import com.yhproject.mywiki.domain.user.UserRepository
import io.jsonwebtoken.Jwts
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

class JwtAuthenticationFilterTest {

    private lateinit var jwtProvider: JwtProvider
    private lateinit var userRepository: UserRepository
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    @BeforeEach
    fun setUp() {
        jwtProvider = mock(JwtProvider::class.java)
        userRepository = mock(UserRepository::class.java)
        jwtAuthenticationFilter = JwtAuthenticationFilter(jwtProvider, userRepository)
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    @DisplayName("유효한 헤더와 토큰이 주어지면 SecurityContext에 사용자 인증 정보가 저장된다")
    fun `valid token populates security context`() {
        // given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        val validToken = "valid.jwt.token"
        request.addHeader("Authorization", "Bearer $validToken")

        val claims = Jwts.claims().subject("1").build()

        val stubUser =
                User(
                        id = 1L,
                        name = "Test User",
                        email = "test@example.com",
                        role = Role.USER,
                        provider = "google",
                        providerId = "12345"
                )

        `when`(jwtProvider.validateToken(validToken)).thenReturn(true)
        `when`(jwtProvider.getClaims(validToken)).thenReturn(claims)
        `when`(userRepository.findById(1L)).thenReturn(stubUser)

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        // then
        val auth = SecurityContextHolder.getContext().authentication
        assertThat(auth).isNotNull()
        assertThat(auth.isAuthenticated).isTrue()

        val principal = auth.principal as PrincipalDetails
        assertThat(principal.user.id).isEqualTo(1L)
        assertThat(principal.user.name).isEqualTo("Test User")
        assertThat(principal.user.email).isEqualTo("test@example.com")
    }

    @Test
    @DisplayName("Authorization 헤더가 없으면 SecurityContext는 비어 있는다")
    fun `missing header leaves security context empty`() {
        // given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        // then
        val auth = SecurityContextHolder.getContext().authentication
        assertThat(auth).isNull()
    }

    @Test
    @DisplayName("유효하지 않은 토큰이면 SecurityContext는 비어 있는다")
    fun `invalid token leaves security context empty`() {
        // given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        val invalidToken = "invalid.token"
        request.addHeader("Authorization", "Bearer $invalidToken")

        `when`(jwtProvider.validateToken(invalidToken)).thenReturn(false)

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        // then
        val auth = SecurityContextHolder.getContext().authentication
        assertThat(auth).isNull()
    }

    @Test
    @DisplayName("토큰은 유효하지만 DB에서 사용자를 찾을 수 없으면 SecurityContext는 비어 있는다")
    fun `valid token but user not found leaves security context empty`() {
        // given
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val filterChain = MockFilterChain()

        val validToken = "valid.jwt.token"
        request.addHeader("Authorization", "Bearer $validToken")

        val claims = Jwts.claims().subject("999").build()

        `when`(jwtProvider.validateToken(validToken)).thenReturn(true)
        `when`(jwtProvider.getClaims(validToken)).thenReturn(claims)
        `when`(userRepository.findById(999L)).thenReturn(null)

        // when
        jwtAuthenticationFilter.doFilter(request, response, filterChain)

        // then
        val auth = SecurityContextHolder.getContext().authentication
        assertThat(auth).isNull()
    }
}
