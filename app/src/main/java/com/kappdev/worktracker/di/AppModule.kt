package com.kappdev.worktracker.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.kappdev.worktracker.core.data.repository.SettingsRepositoryImpl
import com.kappdev.worktracker.core.domain.repository.SettingsRepository
import com.kappdev.worktracker.tracker_feature.data.data_source.WorkDatabase
import com.kappdev.worktracker.tracker_feature.data.repository.*
import com.kappdev.worktracker.tracker_feature.domain.repository.*
import com.kappdev.worktracker.tracker_feature.domain.use_case.GetDailyReportFor
import com.kappdev.worktracker.tracker_feature.domain.use_case.ImportDatabase
import com.kappdev.worktracker.tracker_feature.domain.use_case.ShareDatabase
import com.kappdev.worktracker.tracker_feature.domain.use_case.ZipDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    @ViewModelScoped
    @Named("appDatabase")
    fun provideWorkDatabase(app: Application): WorkDatabase {
        return Room.databaseBuilder(
            app,
            WorkDatabase::class.java,
            WorkDatabase.NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @ViewModelScoped
    @Named("appActivityRepository")
    fun provideActivityRepository(@Named("appDatabase") db: WorkDatabase): ActivityRepository {
        return ActivityRepositoryImpl(db.activityDao)
    }

    @Provides
    @ViewModelScoped
    @Named("appSessionRepository")
    fun provideSessionRepository(@Named("appDatabase") db: WorkDatabase): SessionRepository {
        return SessionRepositoryImpl(db.sessionDao)
    }

    @Provides
    @ViewModelScoped
    @Named("AppSettingsRep")
    fun provideSettingsRepository(@ApplicationContext context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    @Provides
    @ViewModelScoped
    fun provideStatisticRepository(@Named("appDatabase") db: WorkDatabase): StatisticRepository {
        return StatisticRepositoryImpl(db.sessionDao)
    }

    @Provides
    @ViewModelScoped
    @Named("AppReport")
    fun provideGetDailyReportFor(
        @Named("appActivityRepository") activityRepository: ActivityRepository,
        @Named("appSessionRepository") sessionRepository: SessionRepository
    ): GetDailyReportFor {
        return GetDailyReportFor(activityRepository, sessionRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideZipDatabase(app: Application, @Named("appDatabase") db: WorkDatabase): ZipDatabase {
        return ZipDatabase(app, db)
    }

    @Provides
    @ViewModelScoped
    fun provideShareDatabase(app: Application, zipDatabase: ZipDatabase): ShareDatabase {
        return ShareDatabase(app, zipDatabase)
    }

    @Provides
    @ViewModelScoped
    fun provideImportDatabase(app: Application, @Named("appActivityRepository") rep: ActivityRepository): ImportDatabase {
        return ImportDatabase(app, rep)
    }

}