package com.kappdev.worktracker.tracker_feature.data.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.kappdev.worktracker.tracker_feature.domain.model.MinutePoints

class MinutePointsConvertor {

    @TypeConverter
    fun pointsToJson(points: MinutePoints): String = Gson().toJson(points)

    @TypeConverter
    fun jsonToPoints(json: String): MinutePoints = Gson().fromJson(json, MinutePoints::class.java)
}