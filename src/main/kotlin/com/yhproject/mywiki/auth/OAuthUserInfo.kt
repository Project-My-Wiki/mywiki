package com.yhproject.mywiki.auth

import com.yhproject.mywiki.domain.user.Role
import com.yhproject.mywiki.domain.user.User

data class OAuthUserInfo(
    val provider: String,
    val providerId: String,
    val email: String,
    val name: String
) {
    fun toEntity(): User = User(
        name = name,
        email = email,
        role = Role.USER,
        provider = provider,
        providerId = providerId
    )
}
