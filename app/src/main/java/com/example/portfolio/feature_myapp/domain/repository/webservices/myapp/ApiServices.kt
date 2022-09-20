package com.example.demoapp.repository.webservices.myapp

import com.example.demoapp.model.MyApp
import com.example.demoapp.model.MyGit
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiServices {

    @GET("users/myapps")
    suspend fun getMyApps(): List<MyApp>

    @GET("users/younghwanyoon")
    suspend fun getData(): Response<MyGit>
}