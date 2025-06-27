package net.wandroid.todo.security.firebase

import com.google.firebase.auth.FirebaseAuth
import net.wandroid.todo.security.auth.AuthTokenVerifier
import org.springframework.stereotype.Component

@Component
class FirebaseSdkTokenVerifier(
        private val firebaseAuth: FirebaseAuth,
) : AuthTokenVerifier {

    override fun verifyToken(token: String): String? {
        return firebaseAuth.verifyIdToken(token).uid
    }
}
