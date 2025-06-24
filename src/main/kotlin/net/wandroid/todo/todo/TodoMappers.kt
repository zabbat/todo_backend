package net.wandroid.todo.todo

fun TodoEntity.toTodoResponse(): TodoResponse {
    return TodoResponse(
            id = this.id ?: -1L,
            title = this.title,
            content = this.content,
            isDone = this.isDone,
    )
}

fun TodoPatchRequest.toTodoEntity(userId: String): TodoEntity {
    return TodoEntity(
            id = this.id,
            userId = userId,
            title = this.title ?: "",
            content = this.content ?: "",
            isDone = this.isDone ?: false,
    )
}