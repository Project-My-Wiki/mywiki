package com.yhproject.mywiki.auth

import com.yhproject.mywiki.domain.user.Role
import com.yhproject.mywiki.domain.user.User
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Slf4j
class JwtAuthenticationFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        try {
            val token = extractToken(request)
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token!!)) {
                val claims = jwtProvider.getClaims(token)

                val idRaw = claims["id"] ?: claims.subject
                val id = idRaw.toString().toLong()
                val name = claims["name"].toString()
                val email = claims["email"].toString()
                val roleStr = claims["role"].toString()
                val role = if (roleStr == "ROLE_USER") Role.USER else Role.GUEST

                val stubUser =
                        User(
                                id = id,
                                name = name,
                                email = email,
                                role = role,
                                provider = "jwt",
                                providerId = "jwt"
                        )

                val attributesMap = claims.entries.associate { it.key to it.value }.toMutableMap()
                attributesMap["sub"] = claims.subject
                val principalDetails = PrincipalDetails(stubUser, attributesMap, "sub")

                val authentication =
                        UsernamePasswordAuthenticationToken(
                                principalDetails,
                                "",
                                principalDetails.authorities
                        )

                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Authentication failed: ${e.message}")
        }

        filterChain.doFilter(request, response)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}
