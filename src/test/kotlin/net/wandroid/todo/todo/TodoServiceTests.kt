package net.wandroid.todo.todo

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.collections.listOf
import java.util.Optional

@ExtendWith(MockKExtension::class)
class TodoServiceTests {

        @MockK lateinit var todoRepository: TodoRepository
        private lateinit var todoService: TodoService

        private val userId = "userid"

        @BeforeEach
        fun setUp() {
                todoService = TodoService(todoRepository)
        }

        @Test
        fun `addTodo saves a TodoEntity and returns the result as a TodoResponse`() {
                // given
                val request = TodoPatchRequest(title = "title", content = "content", isDone = true)
                val entity =
                        TodoEntity(
                                id = 100,
                                userId = userId,
                                title = "title",
                                content = "content",
                                isDone = true,
                        )

                every { todoRepository.save(any()) } returns entity
                // when
                val result =
                        todoService.addTodo(
                                userId = userId,
                                request = request,
                        )
                // then
                verify { todoRepository.save(any()) }
                val expected = TodoResponse(100, "title", "content", true)
                assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `deleteTodo delete a TodoEntity and returns the deleted id`() {
                // given
                val id = 1L
                justRun { todoRepository.deleteById(id) }
                // when
                todoService.deleteTodo(id)

                // then
                verify { todoRepository.deleteById(id) }
        }

        @Test
        fun `getTodosByUser returns all users todos  as a TodoResponse list`() {
                // given
                val userId = "userId"
                val entity =
                        TodoEntity(
                                id = 100,
                                userId = userId,
                                title = "title",
                                content = "content",
                                isDone = true,
                        )

                every { todoRepository.findByUserId(userId) } returns listOf(entity)
                // when
                val result = todoService.getTodosByUser(userId)
                // then
                verify { todoRepository.findByUserId(userId) }
                val expected = listOf(TodoResponse(100, "title", "content", true))
                assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `updateTodo updates an TodoEntity and returns the updated result as a TodoResponse`() {
                // given
                val id = 100L
                val request = TodoPatchRequest(id = id, title = "title", content = "content", isDone = true)
                val entity =
                        TodoEntity(
                                id = id,
                                userId = userId,
                                title = "title",
                                content = "content",
                                isDone = true,
                        )
                val existingEntity =
                        TodoEntity(
                                id = id,
                                userId = userId,
                                title = "oldtitle",
                                content = "oldcontent",
                                isDone = false,
                        )

                every { todoRepository.findById(id) } returns Optional.of(existingEntity)
                every { todoRepository.save(any()) } returns entity
                // when
                val result =
                        todoService.updateTodo(request)
                // then
                verify { todoRepository.save(any()) }
                val expected = TodoResponse(100, "title", "content", true)
                assertThat(result).isEqualTo(expected)
        }
}
