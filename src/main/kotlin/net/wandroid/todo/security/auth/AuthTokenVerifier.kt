package net.wandroid.todo.security.auth

interface AuthTokenVerifier {
    /**
     * Verifies the provided token and returns the user ID if valid.
     *
     * @param token The authentication token to verify.
     * @return The user ID associated with the token if valid, or null if invalid.
     */
    fun verifyToken(token: String): String?
}