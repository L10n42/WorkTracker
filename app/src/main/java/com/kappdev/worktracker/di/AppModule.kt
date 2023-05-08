package com.kappdev.worktracker.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase
import com.kappdev.worktracker.tracker_feature.data.repository.*
import com.kappdev.worktracker.tracker_feature.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("appDatabase")
    fun provideWorkDatabase(app: Application): WorkDatabase {
        return Room.databaseBuilder(
            app,
            WorkDatabase::class.java,
            WorkDatabase.NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    @Named("appActivityRepository")
    fun provideActivityRepository(@Named("appDatabase") db: WorkDatabase): ActivityRepository {
        return ActivityRepositoryImpl(db.activityDao)
    }

    @Provides
    @Singleton
    @Named("appSessionRepository")
    fun provideSessionRepository(@Named("appDatabase") db: WorkDatabase): SessionRepository {
        return SessionRepositoryImpl(db.sessionDao)
    }

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
    @Named("AppSettingsRep")
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideStatisticRepository(@Named("appDatabase") db: WorkDatabase): StatisticRepository {
        return StatisticRepositoryImpl(db.sessionDao)
    }

}