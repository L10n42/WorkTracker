package com.kappdev.worktracker.tracker_feature.data.data_source

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.kappdev.worktracker.tracker_feature.domain.model.Activity
import com.kappdev.worktracker.tracker_feature.domain.model.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: Session): Long

    @Query("SELECT * FROM sessions WHERE id=:id")
    fun getSessionById(id: Long): Session

    @Query("SELECT * FROM sessions WHERE activity_id=:id")
    fun getSessionsByActivity(id: Long): List<Session>

    @Query("DELETE FROM sessions WHERE activity_id=:id")
    fun deleteSessionsByActivity(id: Long)

    @Query("DELETE FROM sessions WHERE id=:id")
    fun deleteSessionById(id: Long)

    @Delete
    suspend fun deleteSession(session: Session): Int

    @RawQuery
    fun getSessionsFor(query: SupportSQLiteQuery): List<Session>
}