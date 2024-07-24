package com.example.alextimofeev_android_hw18.map.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AttractionItem(
    val type: String,
    val id: Long,
    val lat: Double,
    val lon: Double,
    val tags: Map<String, String>
) : Parcelable