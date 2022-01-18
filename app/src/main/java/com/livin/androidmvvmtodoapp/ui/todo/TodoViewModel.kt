package com.livin.androidmvvmtodoapp.ui.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.livin.androidmvvmtodoapp.R
import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.data.repositories.todo.TodoItemRepository
import com.livin.androidmvvmtodoapp.utils.DispatcherProvider
import com.livin.androidmvvmtodoapp.utils.TodoUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    application: Application,
    private val dispatchers: DispatcherProvider,
    private val todoItemRepository: TodoItemRepository
) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val _todoDetailState = MutableSharedFlow<TodoUiState<TodoItem>>()
    val todoDetailsState = _todoDetailState.asSharedFlow()
    private val _todoCrudState = MutableSharedFlow<TodoUiState<TodoItem>>()
    val todoCrudState = _todoCrudState.asSharedFlow()

    private fun insertTodoToDb(todoItem: TodoItem): Flow<Long> {
        return flow {
            emit(
                todoItemRepository.insertTodoItem(
                    todoItem
                )
            )
        }
    }


    private fun updateTodoToDb(todoItem: TodoItem) =
        viewModelScope.launch(dispatchers.io) {
            todoItemRepository.updateTodoItem(
                todoItem
            )
        }


    private fun deleteTodoFromDB(id: Int) =
        viewModelScope.launch(dispatchers.io) {
            todoItemRepository.deleteTodoItem(
                id
            )
        }


    fun getTodoDetails(id: Int) = viewModelScope.launch(dispatchers.main) {
        _todoDetailState.emit(TodoUiState.Loading)

        val todoItem = todoItemRepository.getTodoItemById(
            id
        )
        todoItem?.let {
            _todoDetailState.emit(TodoUiState.Success(it))
        } ?: kotlin.run {
            _todoDetailState.emit(
                TodoUiState.Error(context.getString(R.string.something_went_wrong))
            )
        }
    }

    fun insertTodoItem(
        id: Int?,
        title: String,
        description: String,
        date: String,
        time: String,
        datetime: Long, type: Int
    ) {
        viewModelScope.launch(dispatchers.main) {
            if (!TodoUtil.isValidTitle(title)) {
                _todoCrudState.emit(
                    TodoUiState.Error(
                        context.getString(R.string.message_title_is_mandatory)
                    )
                )
                return@launch
            }
            if (!TodoUtil.isValidDate(date)) {
                _todoCrudState.emit(
                    TodoUiState.Error(
                        context.getString(R.string.message_date_is_mandatory)
                    )
                )
                return@launch
            }

            if (!TodoUtil.isValidDate(time)) {
                _todoCrudState.emit(
                    TodoUiState.Error(
                        context.getString(R.string.message_time_is_mandatory)
                    )
                )
                return@launch
            }

            if (!TodoUtil.isValidFrequency(type)) {
                _todoCrudState.emit(
                    TodoUiState.Error(
                        context.getString(R.string.message_pls_select_todo_type)
                    )
                )
                return@launch
            }

            _todoCrudState.emit(
                TodoUiState.Loading
            )
            val todoItem = TodoItem(
                id,
                title = title,
                description = description,
                date = datetime,
                time = datetime,
                type = type
            )
            if (id == null) {
                insertTodoToDb(
                    todoItem
                ).collect {
                    _todoCrudState.emit(
                        TodoUiState.Success(
                            TodoItem(
                                it.toInt(),
                                title = title,
                                description = description,
                                date = datetime,
                                time = datetime,
                                type = type
                            )
                        )
                    )
                }
            } else {
                updateTodoToDb(todoItem)
                _todoCrudState.emit(TodoUiState.Pop)
            }
        }
    }

    fun deleteTodo(id: Int) {
        viewModelScope.launch(dispatchers.main) {
            _todoCrudState.emit(
                TodoUiState.Loading
            )
            deleteTodoFromDB(id)
            _todoCrudState.emit(TodoUiState.Pop)
        }
    }

    sealed class TodoUiState<out T> {
        data class Success<T>(val data: T) : TodoUiState<T>()
        data class Error(val message: String) : TodoUiState<Nothing>()
        object Loading : TodoUiState<Nothing>()
        object Pop : TodoUiState<Nothing>()
    }
}