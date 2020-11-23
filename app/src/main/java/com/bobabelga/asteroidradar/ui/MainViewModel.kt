package com.bobabelga.asteroidradar.ui

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bobabelga.asteroidradar.network.AsteroidApi
import com.bobabelga.asteroidradar.network.parseAsteroidsJsonResult
import com.bobabelga.asteroidradar.picofday.PictureOfDay
import com.bobabelga.asteroidradar.room.AsteroidDao
import com.bobabelga.asteroidradar.room.AsteroidEntity
import com.bobabelga.asteroidradar.util.Constants
import com.bobabelga.asteroidradar.util.Constants.API_KEY
import com.bobabelga.asteroidradar.util.Utils
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*


class MainViewModel(
    val asteroidDao: AsteroidDao,
    application: Application
) : AndroidViewModel(application) {


    val endDate = Utils.getfromatedDates(Calendar.getInstance().time)
    var pictureOfDay = MutableLiveData<PictureOfDay>()
    var listAsteroidLiveData = MutableLiveData<List<AsteroidEntity>>()

    init {
        addAllAsteroids()
        addAPictureOfDay()
    }

    fun getTodayAsteroid() {
        viewModelScope.launch {
            listAsteroidLiveData.value = asteroidDao.getTodayAsteroid(endDate)
        }
    }

    fun getWeekAsteroid() {
        viewModelScope.launch {
            listAsteroidLiveData.value = asteroidDao.getAllAsteroid()
        }
    }

    fun addAllAsteroids() {
        viewModelScope.launch {
            getTodayAsteroid()
        }
    }

    fun addAPictureOfDay() {
        viewModelScope.launch {
            getPictureOfDay()
        }
    }


    private suspend fun getPictureOfDay() {
        try {
            pictureOfDay.value = AsteroidApi.retrofitService.getImageOfDay(API_KEY)
        } catch (e: Exception) {
            pictureOfDay.value = PictureOfDay("", "", "")
            Log.d(TAG, "Failure: pictureOfDay ${e.message}")
        }
    }

    fun populateRoomDb(asteroidDao: AsteroidDao) {
        viewModelScope.launch {
            getListAsteroid(asteroidDao)
            listAsteroidLiveData.value = asteroidDao.getTodayAsteroid(endDate)
            Log.d(ContentValues.TAG, "Succes populateRoomDb")
        }
    }

    suspend fun getListAsteroid(asteroidDao: AsteroidDao) {
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
            Log.d(ContentValues.TAG, "Succes fetch")
        } catch (e: Exception) {
            Log.d(ContentValues.TAG, "Failure fetch: ${e.message}")
        }
    }
}

