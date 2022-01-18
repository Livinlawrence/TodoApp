package com.livin.androidmvvmtodoapp.di

import android.content.Context
import androidx.room.Room
import com.livin.androidmvvmtodoapp.data.local.TodoItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
class TestAppModule {

    @Provides
    @Singleton
    @Named("test_db")
    fun provideinMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, TodoItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideTodoItemDao(
        database: TodoItemDatabase
    ) = database.todoItemDao()
}