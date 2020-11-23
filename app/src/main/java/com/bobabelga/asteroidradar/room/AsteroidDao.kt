package com.bobabelga.asteroidradar.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(asteroidEntities:List<AsteroidEntity> )

    @Query("SELECT * from AsteroidEntity ORDER BY closeApproachDate DESC")
    suspend fun getAllAsteroid(): List<AsteroidEntity>?

    @Query("SELECT * from AsteroidEntity WHERE closeApproachDate = :todayDate ORDER BY closeApproachDate DESC" )
    suspend fun getTodayAsteroid(todayDate: String): List<AsteroidEntity>?

}