package com.livin.androidmvvmtodoapp.ui.home

import androidx.lifecycle.ViewModel
import com.livin.androidmvvmtodoapp.data.local.TodoItem
import com.livin.androidmvvmtodoapp.data.repositories.home.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    fun fullTodoItems(): Flow<List<TodoItem>> = homeRepository.getTodoItems()

}