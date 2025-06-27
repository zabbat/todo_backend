package net.wandroid.todo.todo

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.verify
import net.wandroid.todo.security.SecurityConfig
import net.wandroid.todo.security.auth.AuthTokenVerifier
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

// ctrl shift b to run
@WebMvcTest(TodoController::class)
@AutoConfigureMockMvc
@Import(SecurityConfig::class)
class TodoControllerTests {

        @Autowired private lateinit var mvc: MockMvc
        @Autowired lateinit var objectMapper: ObjectMapper

        @MockkBean private lateinit var todoService: TodoService

        @MockkBean lateinit var verifier: AuthTokenVerifier
        private val token = "token"
        private val userId = "userId"

        @BeforeEach
        fun setupAuth() {
                every { verifier.verifyToken(token) } returns userId
        }

        @Test
        @DisplayName("GET /api/todo/list returns current user's todos")
        fun getTodos_returnsListForCurrentUser() {
                val response =
                        TodoResponse(
                                id = 100,
                                title = "title",
                                content = "content",
                                isDone = true,
                        )
                every { todoService.getTodosByUser(userId) } returns listOf(response)

                mvc.perform(get("/api/todo/list").header("Authorization", "Bearer $token"))
                        .andDo(print())
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$[0].id").value(100))
                        .andExpect(jsonPath("$[0].title").value("title"))
                        .andExpect(jsonPath("$[0].content").value("content"))
                        .andExpect(jsonPath("$[0].isDone").value(true))
                verify { todoService.getTodosByUser(userId) }
        }

        @Test
        @DisplayName("GET /api/todo/delete returns deleted todo's id")
        fun deleteTodo_returnsDeletedId() {
                val id = 1L
                justRun { todoService.deleteTodo(id) }

                mvc.perform(
                                get("/api/todo/delete")
                                        .param("id", id.toString())
                                        .header("Authorization", "Bearer $token")
                        )
                        .andDo(print())
                        .andExpect(status().isOk)
                        .andExpect(content().string(id.toString()))
                verify { todoService.deleteTodo(id) }
        }

        @Test
        @DisplayName("POST /api/todo/add returns added todo")
        fun addTodo_returnsAddedTodo() {
                val addRequest =
                        TodoPatchRequest(
                                id = null,
                                title = "title",
                                content = "content",
                                isDone = false,
                        )
                val response =
                        TodoResponse(
                                id = 100L,
                                title = "title",
                                content = "content",
                                isDone = false,
                        )
                every { todoService.addTodo(userId, addRequest) } returns response

                mvc.perform(
                                post("/api/todo/add")
                                        .header("Authorization", "Bearer $token")
                                        .contentType("application/json")
                                        .content(objectMapper.writeValueAsString(addRequest))
                        )
                        .andDo(print())
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").value(100))
                        .andExpect(jsonPath("$.title").value("title"))
                        .andExpect(jsonPath("$.content").value("content"))
                        .andExpect(jsonPath("$.isDone").value(false))
                verify { todoService.addTodo(userId, addRequest) }
        }

        @Test
        @DisplayName("POST /api/todo/update returns updated todo")
        fun updateTodo_returnsUpdatedTodo() {
                val updateRequest =
                        TodoPatchRequest(
                                id = 100L,
                                title = "title",
                                content = "content",
                                isDone = false,
                        )
                val response =
                        TodoResponse(
                                id = 100L,
                                title = "title",
                                content = "content",
                                isDone = false,
                        )
                every { todoService.updateTodo(updateRequest) } returns response

                mvc.perform(
                                post("/api/todo/update")
                                        .header("Authorization", "Bearer $token")
                                        .contentType("application/json")
                                        .content(objectMapper.writeValueAsString(updateRequest))
                        )
                        .andDo(print())
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").value(100))
                        .andExpect(jsonPath("$.title").value("title"))
                        .andExpect(jsonPath("$.content").value("content"))
                        .andExpect(jsonPath("$.isDone").value(false))
                verify { todoService.updateTodo(updateRequest) }
        }
}
