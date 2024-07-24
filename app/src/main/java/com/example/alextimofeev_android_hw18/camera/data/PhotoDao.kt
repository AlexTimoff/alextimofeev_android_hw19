package com.example.alextimofeev_android_hw18.camera.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.alextimofeev_android_hw18.camera.entity.Photo
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    // Выбор всех имеющихся фото в базе
    @Query("SELECT * FROM allPhotos")
    fun getAllPhotos(): Flow<List<Photo>>


    //Добавить фото
    @Insert
    suspend fun insert(photo: Photo)

    //Удалить фото
    @Delete
    suspend fun delete(photo: Photo)

}