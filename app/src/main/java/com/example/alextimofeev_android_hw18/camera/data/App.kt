package com.example.alextimofeev_android_hw18.camera.data

import android.app.Application
import androidx.room.Room
import com.yandex.mapkit.MapKitFactory

class App: Application() {
    lateinit var db: AppDatabase
    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "db"
        ).build()


        //Ключ для MapKit
        MapKitFactory.setApiKey("564dea35-7f19-4a31-aa1f-4aae4d31a831")
        MapKitFactory.initialize(this)
    }
}