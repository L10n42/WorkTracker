package com.kappdev.worktracker.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.kappdev.worktracker.core.data.repository.SettingsRepositoryImpl
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase
import com.kappdev.worktracker.tracker_feature.data.repository.ActivityRepositoryImpl
import com.kappdev.worktracker.tracker_feature.data.repository.SessionRepositoryImpl
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetDailyReportFor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
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

    @Provides
    @Singleton
    @Named("SingletonDatabase")
    fun provideWorkDatabase(app: Application): WorkDatabase {
        return Room.databaseBuilder(
            app,
            WorkDatabase::class.java,
            WorkDatabase.NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    @Named("SingletonActivityRepository")
    fun provideActivityRepository(@Named("SingletonDatabase") db: WorkDatabase): ActivityRepository {
        return ActivityRepositoryImpl(db.activityDao)
    }

    @Provides
    @Singleton
    @Named("SingletonSessionRepository")
    fun provideSessionRepository(@Named("SingletonDatabase") db: WorkDatabase): SessionRepository {
        return SessionRepositoryImpl(db.sessionDao)
    }

    @Provides
    @Singleton
    @Named("SingletonReport")
    fun provideGetDailyReportFor(
        @Named("SingletonActivityRepository") activityRepository: ActivityRepository,
        @Named("SingletonSessionRepository") sessionRepository: SessionRepository
    ): GetDailyReportFor {
        return GetDailyReportFor(activityRepository, sessionRepository)
    }

}