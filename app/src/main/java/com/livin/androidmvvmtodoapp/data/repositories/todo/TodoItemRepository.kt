package com.livin.androidmvvmtodoapp.data.repositories.todo

import com.livin.androidmvvmtodoapp.data.local.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemRepository {

    suspend fun insertTodoItem(todoItem: TodoItem): Long

    suspend fun updateTodoItem(todoItem: TodoItem)

    suspend fun deleteTodoItem(id: Int)

    suspend fun getTodoItemById(id: Int): TodoItem?

    fun getTodoItems(): Flow<List<TodoItem>>
}