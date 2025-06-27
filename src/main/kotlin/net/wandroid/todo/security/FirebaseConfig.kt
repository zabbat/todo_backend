package net.wandroid.todo.security

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import java.io.File
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FirebaseConfig(
        @Value("\${firebase.credentials}") private val credentialsPath: String,
) {
    @Bean
    fun firebaseApp(options: FirebaseOptions): FirebaseApp {
        return if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        } else {
            FirebaseApp.getInstance()
        }
    }

    @Bean
    fun firebaseAuth(firebaseApp: FirebaseApp): FirebaseAuth {
        return FirebaseAuth.getInstance(firebaseApp)
    }

    @Bean
    fun firebaseOptions(): FirebaseOptions {
        val stream =
                if (credentialsPath.startsWith("classpath:")) {
                    javaClass.getResourceAsStream(
                            credentialsPath.removePrefix(prefix = "classpath:")
                    )
                            ?: error("Firebase credentials not found at $credentialsPath")
                } else {
                    File(credentialsPath).inputStream()
                }
        // If you have multiple projects (dev/qa/prod) then you can provide the project ID as well
        // with .setProjectId(projectId)
        // and inject it the same way as for credentialsPath
        val options =
                FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(stream))
                        .build()
        return options
    }
}
