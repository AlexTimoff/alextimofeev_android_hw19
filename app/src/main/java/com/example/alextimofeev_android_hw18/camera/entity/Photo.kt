package com.example.alextimofeev_android_hw18.camera.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "allPhotos")
data class Photo(

//Уникальное поле
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "uri")
    val uri: String,

    @ColumnInfo(name = "date")
    val datePhoto: String

)
