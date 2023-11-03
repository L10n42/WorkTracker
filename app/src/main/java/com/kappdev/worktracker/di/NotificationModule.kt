package com.kappdev.worktracker.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase
import com.kappdev.worktracker.tracker_feature.data.repository.ActivityRepositoryImpl
import com.kappdev.worktracker.tracker_feature.data.repository.SessionRepositoryImpl
import com.kappdev.worktracker.core.data.repository.SettingsRepositoryImpl
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.data.data_source.MIGRATION_8_10
import com.kappdev.worktracker.tracker_feature.domain.use_case.DoneNotification
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Named

@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @Provides
    @Named("serviceDatabase")
    @ServiceScoped
    fun provideWorkDatabase(app: Application): WorkDatabase {
        return Room.databaseBuilder(
            app,
            WorkDatabase::class.java,
            WorkDatabase.NAME
        ).build()
    }

    @Provides
    @Named("serviceActivityRepository")
    @ServiceScoped
    fun provideActivityRepository(@Named("serviceDatabase") db: WorkDatabase): ActivityRepository {
        return ActivityRepositoryImpl(db.activityDao)
    }

    @Provides
    @Named("serviceSessionRepository")
    @ServiceScoped
    fun provideSessionRepository(@Named("serviceDatabase") db: WorkDatabase): SessionRepository {
        return SessionRepositoryImpl(db.sessionDao)
    }

    @Provides
    @Named("ServiceNotificationManager")
    @ServiceScoped
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Named("ServiceSettingsRep")
    @ServiceScoped
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    @Provides
    @ServiceScoped
    fun provideDoneNotification(
        context: Application,
        @Named("ServiceNotificationManager") notificationManager: NotificationManager
    ): DoneNotification {
        return DoneNotification(context, notificationManager)
    }
}