package com.yhproject.mywiki.auth

import com.yhproject.mywiki.domain.user.User
import com.yhproject.mywiki.domain.user.UserRepository
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val oAuthUserService: OAuthUserService
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val delegate = DefaultOAuth2UserService()
        val oAuth2User = delegate.loadUser(userRequest)

        val registrationId = userRequest.clientRegistration.registrationId
        val userNameAttributeName = userRequest.clientRegistration.providerDetails.userInfoEndpoint.userNameAttributeName

        val attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.attributes)

        // 사용자 저장/업데이트를 공통 서비스에 위임
        val user: User = oAuthUserService.saveOrUpdate(attributes.toUserInfo())

        return PrincipalDetails(
            user = user,
            attributes = attributes.attributes,
            nameAttributeKey = attributes.nameAttributeKey
        )
    }
}
