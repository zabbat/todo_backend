package net.wandroid.todo.todo

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

data class TodoPatchRequest(
        val id: Long? = Unchanged,
        val title: String? = Unchanged,
        val content: String? = Unchanged,
        val isDone: Boolean? = Unchanged,
)

val Unchanged = null