package com.kappdev.worktracker.di

import android.app.Application
import android.app.NotificationManager
import androidx.core.content.ContextCompat
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmReceiverModule {

    @Provides
    @Named("SingletonNotificationManager")
    @Singleton
    fun provideNotificationManager(app: Application): NotificationManager {
        return ContextCompat.getSystemService(app, NotificationManager::class.java) as NotificationManager
    }

}