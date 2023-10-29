package com.kappdev.worktracker.di

import android.content.Context
import com.kappdev.worktracker.core.data.repository.SettingsRepositoryImpl
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.data.repository.CountdownControllerImpl
import com.kappdev.worktracker.tracker_feature.data.repository.StopwatchControllerImpl
import com.kappdev.worktracker.tracker_feature.domain.repository.CountdownController
import com.kappdev.worktracker.tracker_feature.domain.repository.StopwatchController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Provides
    @Singleton
    fun provideStopwatchRepository(@ApplicationContext context: Context): StopwatchController {
        return StopwatchControllerImpl(context)
    }

    @Provides
    @Singleton
    fun provideCountdownRepository(@ApplicationContext context: Context): CountdownController {
        return CountdownControllerImpl(context)
    }

    @Provides
    @Singleton
    @Named("SingletonAppSettingsRep")
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}