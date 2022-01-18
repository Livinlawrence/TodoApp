package com.livin.androidmvvmtodoapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodoItem(todoItem: TodoItem): Long

    @Update
    suspend fun updateTodoItem(todoItem: TodoItem)

    @Query("DELETE FROM todo_item WHERE id=:id")
    suspend fun deleteTodoItem(id: Int)

    @Query("SELECT * FROM todo_item")
    fun getTodoItems(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_item WHERE id = :id")
    fun getTodoItemById(id: Int): TodoItem?
}