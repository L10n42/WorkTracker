package com.kappdev.worktracker.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase
import com.kappdev.worktracker.tracker_feature.data.repository.ActivityRepositoryImpl
import com.kappdev.worktracker.tracker_feature.data.repository.SessionRepositoryImpl
import com.kappdev.worktracker.tracker_feature.domain.repository.ActivityRepository
import com.kappdev.worktracker.tracker_feature.domain.repository.SessionRepository
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
        ).fallbackToDestructiveMigration().build()
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
    @ServiceScoped
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @ServiceScoped
    fun provideDoneNotification(
        context: Application,
        notificationManager: NotificationManager
    ): DoneNotification {
        return DoneNotification(context, notificationManager)
    }
}