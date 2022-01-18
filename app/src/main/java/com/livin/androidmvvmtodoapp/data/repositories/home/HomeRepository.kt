package com.livin.androidmvvmtodoapp.data.repositories.home

import com.livin.androidmvvmtodoapp.data.local.TodoItem
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getTodoItems(): Flow<List<TodoItem>>
}