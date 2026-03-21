package com.yhproject.mywiki.auth

import com.yhproject.mywiki.domain.user.Role
import com.yhproject.mywiki.domain.user.User
import com.yhproject.mywiki.domain.user.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

class OAuthUserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var oAuthUserService: OAuthUserService

    private val mockUserInfo = OAuthUserInfo(
        provider = "google",
        providerId = "google-123",
        email = "test@example.com",
        name = "테스트 유저"
    )

    @BeforeEach
    fun setUp() {
        userRepository = mock(UserRepository::class.java)
        oAuthUserService = OAuthUserService(userRepository)
    }

    @Test
    @DisplayName("신규 사용자의 경우 DB에 저장한다")
    fun `new user is saved to database`() {
        // given
        whenever(userRepository.findByEmail("test@example.com")).thenReturn(null)
        val newUser = User(
            id = 1L,
            name = "테스트 유저",
            email = "test@example.com",
            role = Role.USER,
            provider = "google",
            providerId = "google-123"
        )
        whenever(userRepository.save(any())).thenReturn(newUser)

        // when
        val result = oAuthUserService.saveOrUpdate(mockUserInfo)

        // then
        assertThat(result.email).isEqualTo("test@example.com")
        verify(userRepository).save(any())
    }

    @Test
    @DisplayName("기존 사용자의 경우 이름을 업데이트하고 저장한다")
    fun `existing user name is updated`() {
        // given
        val existingUser = User(
            id = 1L,
            name = "기존 이름",
            email = "test@example.com",
            role = Role.USER,
            provider = "google",
            providerId = "google-123"
        )
        whenever(userRepository.findByEmail("test@example.com")).thenReturn(existingUser)
        whenever(userRepository.save(any())).thenReturn(existingUser)

        // when
        val result = oAuthUserService.saveOrUpdate(mockUserInfo)

        // then
        assertThat(result.name).isEqualTo("테스트 유저")
        verify(userRepository).save(existingUser)
    }
}
