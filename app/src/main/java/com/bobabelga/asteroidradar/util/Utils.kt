package com.bobabelga.asteroidradar.util

import android.content.ContentValues
import android.util.Log
import com.bobabelga.asteroidradar.network.AsteroidApi
import com.bobabelga.asteroidradar.network.parseAsteroidsJsonResult
import com.bobabelga.asteroidradar.room.AsteroidDao
import com.bobabelga.asteroidradar.room.AsteroidDatabase
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

object Utils {

    fun getfromatedDates(date: Date): String {
        var dateFromated: String
        SimpleDateFormat(
            Constants.API_QUERY_DATE_FORMAT,
            Locale.getDefault()
        ).also {
            dateFromated = it.format(date)
        }
        return dateFromated
    }
}