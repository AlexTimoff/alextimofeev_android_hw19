package com.example.alextimofeev_android_hw18.map.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alextimofeev_android_hw18.map.data.PostAllAttractionsApi
import com.example.alextimofeev_android_hw18.map.entity.AttractionItem
import com.example.alextimofeev_android_hw18.map.entity.ListAttractions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(
    private val api: PostAllAttractionsApi
): ViewModel() {

    val attractionsData: MutableLiveData<List<AttractionItem>> by lazy {
        MutableLiveData<List<AttractionItem>>()
    }

    fun takeAttractions() {
        val requestBody = createRequestBody()
        val call = api.postAttractions(requestBody)

        call.enqueue(object : Callback<ListAttractions> {
            override fun onResponse(
                call: Call<ListAttractions>,
                response: Response<ListAttractions>
            ) {
                if (response.isSuccessful) {
                    val listAttractions = response.body()
                    listAttractions?.items?.let {
                        Log.d("MapViewModel", "Received attractions: $it")
                        attractionsData.value = it

                    }
                } else {
                    Log.e("MapActivity", "Failed to take attractions: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ListAttractions>, t: Throwable) {
                Log.e("MapActivity", "Failed to fetch landmarks: ${t.message}")
            }
        })
    }

    private fun createRequestBody(): RequestBody {
        val query = "[out:json];node[\"tourism\"=\"museum\"](55.55,37.35,55.95,37.85);out;"
        return RequestBody.create("application/x-www-form-urlencoded"
            .toMediaTypeOrNull(), "data=$query")
    }

}