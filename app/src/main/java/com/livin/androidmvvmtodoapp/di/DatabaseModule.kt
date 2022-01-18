package com.livin.androidmvvmtodoapp.di

import android.content.Context
import androidx.room.Room
import com.livin.androidmvvmtodoapp.data.local.TodoItemDatabase
import com.livin.androidmvvmtodoapp.other.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, TodoItemDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideTodoItemDao(
        database: TodoItemDatabase
    ) = database.todoItemDao()

}