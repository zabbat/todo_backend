package net.wandroid.todo.todo

import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/todo")
public class TodoController(
        private val todoService: TodoService,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(TodoController::class.java)
    }

    @GetMapping("/listAll") fun getAllTodos(): List<TodoResponse> = todoService.getAllTodos()

    @GetMapping("/list")
    fun getTodos(
            authentication: Authentication,
    ): List<TodoResponse> {
        val userId = authentication.name
        return todoService.getTodosByUser(userId)
    }

    @GetMapping("/delete")
    fun deleteTodo(
            @RequestParam(value = "id", required = true) id: Long,
    ): Long {
        todoService.deleteTodo(id)
        return id
    }

    @PostMapping("/add")
    fun addTodo(
            @RequestBody todoPatchRequestData: TodoPatchRequest,
            authentication: Authentication,
    ): TodoResponse {
        val userId = authentication.name
        return todoService.addTodo(userId, todoPatchRequestData)
    }

    @PostMapping("/update")
    fun updateTodo(@RequestBody todoPatchRequest: TodoPatchRequest): TodoResponse {
        return todoService.updateTodo(request = todoPatchRequest)
    }

    /*

     @PatchMapping("/{id}")
        fun updatePerson(
            @PathVariable id: Long,
            @RequestBody update: PersonPatchRequest
        ): ResponseEntity<PersonResponse> {
            val updated = personService.updatePerson(id, update)
            return ResponseEntity.ok(updated)
        }
    */

}
