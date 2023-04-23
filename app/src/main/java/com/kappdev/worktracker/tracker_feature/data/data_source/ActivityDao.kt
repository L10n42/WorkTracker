package com.kappdev.worktracker.tracker_feature.data.data_source

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: Activity): Long

    @Query("SELECT * FROM activities")
    fun getActivities(): Flow<List<Activity>>

    @RawQuery
    fun getWithOrder(query: SupportSQLiteQuery): List<Activity>

    @Query("SELECT * FROM activities WHERE id=:id")
    fun getActivityById(id: Long): Activity

    @Query("DELETE FROM activities WHERE id=:id")
    fun deleteActivityById(id: Long)

    @Delete
    suspend fun deleteActivity(activity: Activity): Int
}