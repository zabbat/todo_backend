package net.wandroid.todo.todo


data class TodoResponse(
        val id: Long,
        val title: String,
        val content: String,
        val isDone: Boolean,
)
