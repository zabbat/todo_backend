package net.wandroid.todo.security.firebase

import com.google.firebase.auth.FirebaseAuth
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.wandroid.todo.security.auth.AuthTokenVerifier
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class FirebaseAuthenticationFilter(
        private val verifier: AuthTokenVerifier,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader?.startsWith("Bearer") == true) {
            val token = authHeader.removePrefix("Bearer").trim()
            try {
                val uid = verifier.verifyToken(token)
                        ?: throw IllegalArgumentException("Invalid token")
                val authentication =
                        UsernamePasswordAuthenticationToken(
                                uid,
                                null,
                                listOf(
                                        // SimpleGrantedAuthority("ROLE_USER"),
                                        ),
                        )

                SecurityContextHolder.getContext().authentication = authentication
            } catch (e: Exception) {

                response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid Firebase token: ${e.message}"
                )
                return
            }
        } else {
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing Authorization/Bearer header"
            )
            return
        }
        filterChain.doFilter(request, response)
    }
}
