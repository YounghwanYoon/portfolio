package com.example.demoapp.repository.webservices.myapp

import com.example.demoapp.model.MyApp
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiServices {

    @GET("user/myapps")
    suspend fun getMyApps(): List<MyApp>
}