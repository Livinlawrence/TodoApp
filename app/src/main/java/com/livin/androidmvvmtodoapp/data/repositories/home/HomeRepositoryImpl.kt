package com.livin.androidmvvmtodoapp.data.repositories.home

import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.data.local.TodoItemDao
import com.livin.androidmvvmtodoapp.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val todoItemDao: TodoItemDao,
    private val dispatchers: DispatcherProvider,
) : HomeRepository {

    override fun getTodoItems(): Flow<List<TodoItem>> {
        return flow {
            todoItemDao.getTodoItems().collect {
                emit(it.asReversed())
            }
        }.flowOn(dispatchers.io)
    }
}