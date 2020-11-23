package com.bobabelga.asteroidradar.util

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bobabelga.asteroidradar.network.AsteroidApi
import com.bobabelga.asteroidradar.network.parseAsteroidsJsonResult
import com.bobabelga.asteroidradar.room.AsteroidDao
import com.bobabelga.asteroidradar.room.AsteroidDatabase
import org.json.JSONObject
import java.util.*

class UpdateDataWorker (appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork: Start")
        val asteroidDao = AsteroidDatabase.getInstance(applicationContext).AsteroidDao
         getListAsteroid(asteroidDao)
        return Result.success()
    }
    private suspend fun getListAsteroid(asteroidDao:AsteroidDao) {
        val endDate = Utils.getfromatedDates(Calendar.getInstance().time)
        val startDate = Utils.getfromatedDates(Calendar.getInstance().also {
            it.add(Calendar.DAY_OF_YEAR, -Constants.DEFAULT_END_DATE_DAYS)
        }.time)
        val map = HashMap<String, String>()
        map.put("start_date", startDate)
        map.put("end_date", endDate)
        map.put("api_key", Constants.API_KEY)
        try {
            val string = AsteroidApi.retrofitService.getAsteroidList(map)
            val reader = JSONObject(string)
            asteroidDao.insert(parseAsteroidsJsonResult(reader))
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Failure: ${e.message}")
        }
    }
}