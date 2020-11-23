package com.bobabelga.asteroidradar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import com.bobabelga.asteroidradar.R
import com.bobabelga.asteroidradar.util.UpdateDataWorker
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = this.findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)

        val myConstraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()
        val myWorkBuilder =
            PeriodicWorkRequest.Builder(UpdateDataWorker::class.java,
                1, TimeUnit.DAYS)
                .setConstraints(myConstraints)

        val myWork = myWorkBuilder.build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("updateData",
                ExistingPeriodicWorkPolicy.KEEP, myWork)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }
}