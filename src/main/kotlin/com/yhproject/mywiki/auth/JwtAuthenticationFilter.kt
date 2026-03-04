package com.yhproject.mywiki.auth

import com.yhproject.mywiki.domain.user.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.extern.slf4j.Slf4j
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Slf4j
class JwtAuthenticationFilter(
        private val jwtProvider: JwtProvider,
        private val userRepository: UserRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        try {
            val token = extractToken(request)
            if (StringUtils.hasText(token) && jwtProvider.validateToken(token!!)) {
                val claims = jwtProvider.getClaims(token)
                val userId = claims.subject.toLong()

                val user = userRepository.findById(userId)
                if (user != null) {
                    val principalDetails = PrincipalDetails(user, emptyMap(), "sub")
                    val authentication =
                            UsernamePasswordAuthenticationToken(
                                    principalDetails,
                                    "",
                                    principalDetails.authorities
                            )
                    SecurityContextHolder.getContext().authentication = authentication
                }
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
