package com.livin.androidmvvmtodoapp.di

import com.livin.androidmvvmtodoapp.data.local.TodoItemDao
import com.livin.androidmvvmtodoapp.data.remote.APIService
import com.livin.androidmvvmtodoapp.data.repositories.home.HomeRepository
import com.livin.androidmvvmtodoapp.data.repositories.home.HomeRepositoryImpl
import com.livin.androidmvvmtodoapp.data.repositories.login.LoginRepository
import com.livin.androidmvvmtodoapp.data.repositories.login.LoginRepositoryImpl
import com.livin.androidmvvmtodoapp.data.repositories.todo.TodoItemRepository
import com.livin.androidmvvmtodoapp.data.repositories.todo.TodoItemRepositoryImpl
import com.livin.androidmvvmtodoapp.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        apiService: APIService,
        dispatcherProvider: DispatcherProvider
    ): LoginRepository {
        return LoginRepositoryImpl(apiService, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(
        todoItemDao: TodoItemDao,
        dispatcherProvider: DispatcherProvider
    ): HomeRepository {
        return HomeRepositoryImpl(todoItemDao, dispatcherProvider)
    }

    @Provides
    @Singleton
    fun provideTodoItemRepository(
        todoItemDao: TodoItemDao,
        dispatcherProvider: DispatcherProvider
    ): TodoItemRepository {
        return TodoItemRepositoryImpl(todoItemDao, dispatcherProvider)
    }
}