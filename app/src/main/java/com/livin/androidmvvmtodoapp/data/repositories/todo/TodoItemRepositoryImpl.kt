package com.livin.androidmvvmtodoapp.data.repositories.todo

import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.data.local.TodoItemDao
import com.livin.androidmvvmtodoapp.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoItemRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val dispatchers: DispatcherProvider,
) : TodoItemRepository {

    override suspend fun insertTodoItem(todoItem: TodoItem): Long {
        return todoItemDao.insertTodoItem(todoItem)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem) {
        todoItemDao.updateTodoItem(todoItem)
    }

    override suspend fun deleteTodoItem(id: Int) {
        todoItemDao.deleteTodoItem(id)
    }

    override suspend fun getTodoItemById(id: Int): TodoItem? {
        return withContext(dispatchers.io) {
            todoItemDao.getTodoItemById(id)
        }
    }

    override fun getTodoItems(): Flow<List<TodoItem>> {
        return todoItemDao.getTodoItems()
    }
}