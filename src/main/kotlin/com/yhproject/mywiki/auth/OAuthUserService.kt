package com.yhproject.mywiki.auth

import com.yhproject.mywiki.domain.user.User
import com.yhproject.mywiki.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 웹(OAuth2 리다이렉트)과 모바일(ID Token) 양 흐름에서 공통으로 사용되는
 * 사용자 저장/업데이트 서비스.
 */
@Service
class OAuthUserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun saveOrUpdate(info: OAuthUserInfo): User {
        return userRepository.findByEmail(info.email)?.update(info.name)
            ?: userRepository.save(info.toEntity())
    }
}
