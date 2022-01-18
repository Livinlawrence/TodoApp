package com.livin.androidmvvmtodoapp.di

import com.livin.androidmvvmtodoapp.utils.DefaultDispatchers
import com.livin.androidmvvmtodoapp.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = DefaultDispatchers()

}