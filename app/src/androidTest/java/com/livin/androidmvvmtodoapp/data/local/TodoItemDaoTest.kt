package com.livin.androidmvvmtodoapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TodoItemDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: TodoItemDatabase
    private lateinit var dao: TodoItemDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoItemDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.todoItemDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTodoItem() = runBlockingTest {
        val todoItem = TodoItem(1, "title", "demo", 1L, 1L, 2)
        dao.insertTodoItem(todoItem)

        val allTodos = dao.getTodoItems().first()
        assertEquals(allTodos[0].title, todoItem.title)
    }


    @Test
    fun deleteTodoItem() = runBlockingTest {
        val todoItem = TodoItem(1, "title", "demo", 1L, 1L, 2)
        dao.insertTodoItem(todoItem)
        dao.deleteTodoItem(1)

        val allTodos = dao.getTodoItems().first()
        assertThat(allTodos).doesNotContain(todoItem)
    }

    @Test
    fun updateTodoItem() = runBlockingTest {
        val todoItem = TodoItem(1, "title", "demo", 1L, 1L, 2)
        dao.insertTodoItem(todoItem)
        todoItem.title = "updated one"
        dao.updateTodoItem(todoItem)

        val allTodos = dao.getTodoItems().first()
        assertThat(allTodos[0].title).isEqualTo("updated one")
    }

}