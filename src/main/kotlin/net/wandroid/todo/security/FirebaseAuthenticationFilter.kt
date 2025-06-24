package net.wandroid.todo.security

import com.google.firebase.auth.FirebaseAuth
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class FirebaseAuthenticationFilter : OncePerRequestFilter() {

    // private val logger = LoggerFactory.getLogger(FirebaseAuthenticationFilter::class.java)

    override fun doFilterInternal(
            request: HttpServletRequest,
            response: HttpServletResponse,
            filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader?.startsWith("Bearer") == true) {
            val token = authHeader.removePrefix("Bearer").trim()
            try {
                val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
                val authentication =
                        UsernamePasswordAuthenticationToken(
                                decodedToken.uid,
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
