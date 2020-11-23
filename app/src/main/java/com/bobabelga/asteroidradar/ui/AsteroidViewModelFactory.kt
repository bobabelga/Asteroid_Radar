package com.bobabelga.asteroidradar.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bobabelga.asteroidradar.room.AsteroidDao

class AsteroidViewModelFactory(
    private val asteroidDao: AsteroidDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(asteroidDao,application) as T
        }
        throw IllegalArgumentException("Unknow ViewModel Class")
    }

}