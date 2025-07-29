package com.house.keeper

import android.app.Application
import com.house.keeper.data.database.DatabaseInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HouseKeeperApplication : Application() {

    @Inject
    lateinit var databaseInitializer: DatabaseInitializer

    override fun onCreate() {
        super.onCreate()
        databaseInitializer.initializeDatabase()
    }
}
