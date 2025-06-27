package net.wandroid.todo

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class TodoApplicationTests {

    @MockkBean(relaxed = true) lateinit var firebaseOptions: FirebaseOptions

    @MockkBean(relaxed = true) lateinit var firebaseAuth: FirebaseAuth

    @MockkBean(relaxed = true) lateinit var firebaseApp: FirebaseApp

    @Test fun contextLoads() {}
}
