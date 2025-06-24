package net.wandroid.todo.todo

import org.springframework.stereotype.Service

@Service
class TodoService(private val todoRepository: TodoRepository) {

    fun getAllTodos(): List<TodoResponse> {
        return todoRepository.findAll().map { entity -> entity.toTodoResponse() }
    }

    fun getTodosByUser(userId: String): List<TodoResponse> =
            todoRepository.findByUserId(userId).map { it.toTodoResponse() }

    fun addTodo(userId: String, request: TodoPatchRequest): TodoResponse =
            todoRepository.save(request.toTodoEntity(userId)).toTodoResponse()

    fun updateTodo(request: TodoPatchRequest): TodoResponse {
        val existing =
                todoRepository.findById(request.id!!).orElseThrow {
                    IllegalArgumentException("Todo not found with id ${request.id}")
                }

        val updated =
                existing.copy(
                        title = request.title ?: existing.title,
                        content = request.content ?: existing.content,
                        isDone = request.isDone ?: existing.isDone,
                )

        return todoRepository.save(updated).toTodoResponse()
    }

    fun deleteTodo(id: Long) {
        todoRepository.deleteById(id)
    }
}
