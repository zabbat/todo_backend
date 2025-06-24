package net.wandroid.todo.todo

import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<TodoEntity, Long> {
    fun findByUserId(userId: String): List<TodoEntity>
}
